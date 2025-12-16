package ru.sinvic.multithreading.user.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sinvic.multithreading.user.report.model.UserInfo;
import ru.sinvic.multithreading.user.report.model.EmployeeReport;
import ru.sinvic.multithreading.user.report.model.EmploymentInfo;
import ru.sinvic.multithreading.user.report.model.SalaryInfo;
import ru.sinvic.multithreading.user.report.service.UserInfoService;
import ru.sinvic.multithreading.user.report.service.EmploymentInfoService;
import ru.sinvic.multithreading.user.report.service.SalaryInfoService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Slf4j
@RequiredArgsConstructor
public class UserReportLoader {

    private final UserInfoService userInfoService;
    private final EmploymentInfoService employmentInfoService;
    private final SalaryInfoService salaryInfoService;

    public CompletableFuture<EmployeeReport> loadReport(long id) throws BasicUserInfoLoadingException {

        CompletableFuture<UserInfo> basicUserInfoCompletableFuture = loadBasicInfo(id);
        CompletableFuture<EmploymentInfo> employmentDataCompletableFuture = loadEmploymentInfo(id);
        CompletableFuture<SalaryInfo> userSalaryInfoCompletableFuture = loadSalaryInfo(id);

        return basicUserInfoCompletableFuture
            .thenCompose(user -> employmentDataCompletableFuture
                .thenCombine(userSalaryInfoCompletableFuture,
                    (e, s) -> new EmployeeReport(user.name(), user.email(), e.position(),
                        e.department(), s.salary(), LocalDateTime.now()))
            );
    }

    private CompletableFuture<Optional<SalaryInfo>> loadSalaryInfo(long id) {
        return salaryInfoService.fetchSalaryInfo(id)
            .thenApply(Optional::of)
            .exceptionally(ex -> Optional.empty());
    }

    private CompletableFuture<Optional<EmploymentInfo>> loadEmploymentInfo(long id) {
        return employmentInfoService.fetchEmploymentData(id)
            .thenApply(Optional::of)
            .exceptionally(_ -> Optional.empty());
    }

    private CompletableFuture<UserInfo> loadBasicInfo(long id) {
        return userInfoService.fetchBasicInfo(id)
            .exceptionally(ex -> {
                throw new BasicUserInfoLoadingException(ex.getMessage());
            });
    }
}
