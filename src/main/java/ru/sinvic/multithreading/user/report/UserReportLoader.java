package ru.sinvic.multithreading.user.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sinvic.multithreading.user.report.model.BasicUserInfo;
import ru.sinvic.multithreading.user.report.model.EmployeeReport;
import ru.sinvic.multithreading.user.report.model.EmploymentData;
import ru.sinvic.multithreading.user.report.model.UserSalaryInfo;
import ru.sinvic.multithreading.user.report.service.UserInfoService;
import ru.sinvic.multithreading.user.report.service.EmploymentDataService;
import ru.sinvic.multithreading.user.report.service.SalaryInfoService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@Slf4j
@RequiredArgsConstructor
public class UserReportLoader {

    private final UserInfoService userInfoService;
    private final EmploymentDataService employmentDataService;
    private final SalaryInfoService salaryInfoService;

    public CompletableFuture<EmployeeReport> loadReport(long id) throws BasicUserInfoLoadingException {

        CompletableFuture<BasicUserInfo> basicUserInfoCompletableFuture = loadBasicInfo(id);
        CompletableFuture<EmploymentData> employmentDataCompletableFuture = loadEmploymentInfo(id);
        CompletableFuture<UserSalaryInfo> userSalaryInfoCompletableFuture = loadSalaryInfo(id);

        return basicUserInfoCompletableFuture
            .thenCompose(user -> employmentDataCompletableFuture
                .thenCombine(userSalaryInfoCompletableFuture,
                    (e, s) -> new EmployeeReport(user.name(), user.email(), e.position(),
                        e.department(), s.salary(), LocalDateTime.now()))
            );
    }

    private CompletableFuture<Optional<UserSalaryInfo>> loadSalaryInfo(long id) {
        return salaryInfoService.fetchSalaryInfo(id)
            .thenApply(Optional::of)
            .exceptionally(ex -> Optional.empty());
    }

    private CompletableFuture<Optional<EmploymentData>> loadEmploymentInfo(long id) {
        return employmentDataService.fetchEmploymentData(id)
            .thenApply(Optional::of)
            .exceptionally(_ -> Optional.empty());
    }

    private CompletableFuture<BasicUserInfo> loadBasicInfo(long id) {
        return userInfoService.fetchBasicInfo(id)
            .exceptionally(ex -> {
                throw new BasicUserInfoLoadingException(ex.getMessage());
            });
    }
}
