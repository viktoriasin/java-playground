package ru.sinvic.multithreading.userdatareport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sinvic.multithreading.userdatareport.model.BasicUserInfo;
import ru.sinvic.multithreading.userdatareport.model.EmployeeReport;
import ru.sinvic.multithreading.userdatareport.model.EmploymentData;
import ru.sinvic.multithreading.userdatareport.model.UserSalaryInfo;
import ru.sinvic.multithreading.userdatareport.service.BasicUserInfoLoaderService;
import ru.sinvic.multithreading.userdatareport.service.EmploymentDataLoaderService;
import ru.sinvic.multithreading.userdatareport.service.SalaryInfoLoaderService;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;


@Slf4j
@RequiredArgsConstructor
public class UserReportLoader {

    private final BasicUserInfoLoaderService basicUserInfoLoaderService;
    private final EmploymentDataLoaderService employmentDataLoaderService;
    private final SalaryInfoLoaderService salaryInfoLoaderService;

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

    private CompletableFuture<UserSalaryInfo> loadSalaryInfo(long id) {
        return salaryInfoLoaderService.fetchSalaryInfo(id).exceptionally(_ -> new UserSalaryInfo(id, null));
    }

    private CompletableFuture<EmploymentData> loadEmploymentInfo(long id) {
        return employmentDataLoaderService.fetchEmploymentData(id).exceptionally(_ -> new EmploymentData(id, null, null));
    }

    private CompletableFuture<BasicUserInfo> loadBasicInfo(long id) {
        return basicUserInfoLoaderService.fetchBasicInfo(id)
            .exceptionally(ex -> {
                throw new BasicUserInfoLoadingException(ex.getMessage());
            });
    }
}
