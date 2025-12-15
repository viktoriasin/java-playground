package ru.sinvic.multithreading;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ParallelWebScraper {
    private final int poolSize;
    private final long taskTimeout;
    private final WebPageProcessor webPageProcessor = new WebPageProcessor();

    public List<ProcessingResult> processPages(List<String> urls) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(poolSize); // В идеале лучше использвоать Semaphore для полного контроля над количеством отправляемых http запросов
        try {
            Map<String, CompletableFuture<ProcessingResult>> futureResultsOfProcessingUrls =
                submitTasks(urls, executor);

            return getProcessingResults(futureResultsOfProcessingUrls);
        } finally {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    private List<ProcessingResult> getProcessingResults(Map<String, CompletableFuture<ProcessingResult>> futureResultsOfProcessingUrls) {
        return futureResultsOfProcessingUrls.entrySet().stream()
            .map(entry -> {
                try {
                    CompletableFuture<ProcessingResult> value = entry.getValue();
                    return value.orTimeout(taskTimeout, TimeUnit.SECONDS).join();
                } catch (CompletionException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof TimeoutException) {
                        return new ProcessingResult(entry.getKey(), null, (TimeoutException) cause);
                    } else {
                        return new ProcessingResult(entry.getKey(), null, e);
                    }
                }
            })
            .toList();
    }

    private Map<String, CompletableFuture<ProcessingResult>> submitTasks(List<String> urls, ExecutorService executor) {
        return urls.stream()
            .collect(Collectors.toMap(
                url -> url,
                url -> CompletableFuture.supplyAsync(
                    () -> webPageProcessor.fetchTitle(url),
                    executor
                )));
    }
}

record ProcessingResult(String url, String title, Exception error) {
}

class WebPageProcessor {
    private final ConcurrentHashMap<String, ProcessingResult> cache = new ConcurrentHashMap<>();
    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(5))
        .build();

    public ProcessingResult fetchTitle(String urlString) {
        return cache.computeIfAbsent(urlString, this::doFetchTitle);
    }

    public ProcessingResult doFetchTitle(String urlString) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .timeout(Duration.ofSeconds(10))
                .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String title = Jsoup.parse(response.body()).title();
            return new ProcessingResult(urlString, title, null);

        } catch (Exception e) {
            return new ProcessingResult(urlString, null, e);
        }
    }
}
