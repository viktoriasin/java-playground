package ru.sinvic.datamanipulation.memorycachewithttl;


//В качестве готовых реализаций можно использовать Guava CacheBuilder или Caffeine
public class TestCache {
    public static void main(String[] args) throws InterruptedException {
        ClosableCache cache = new InMemoryCacheWithScheduledTtl(1);

        String key = "First";
        Integer value = 1;
        cache.put(key, value);
        Thread.sleep(1000);
        System.out.println(cache.get(key));

        cache.put(key, 2);
        Thread.sleep(100);
        System.out.println(cache.get(key));
        cache.close();
    }
}
