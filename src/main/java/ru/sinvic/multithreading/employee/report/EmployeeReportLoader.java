package ru.sinvic.multithreading.employee.report;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.sinvic.multithreading.employee.report.exceptions.EmploymentInfoException;
import ru.sinvic.multithreading.employee.report.exceptions.ReportError;
import ru.sinvic.multithreading.employee.report.exceptions.SalaryInfoException;
import ru.sinvic.multithreading.employee.report.exceptions.UserInfoException;
import ru.sinvic.multithreading.employee.report.model.*;
import ru.sinvic.multithreading.employee.report.service.EmploymentInfoService;
import ru.sinvic.multithreading.employee.report.service.SalaryInfoService;
import ru.sinvic.multithreading.employee.report.service.UserInfoService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;


@Slf4j
@RequiredArgsConstructor
public class EmployeeReportLoader {

    private final UserInfoService userInfoService;
    private final EmploymentInfoService employmentInfoService;
    private final SalaryInfoService salaryInfoService;

    public CompletableFuture<EmployeeReportResult> loadReport(long id) {

        CompletableFuture<UserInfoResult> optionalCompletableFuture = loadUserInfo(id);

        return optionalCompletableFuture.thenCompose(userInfoResult -> {
                if (userInfoResult.userInfoError() != null) {
                    return CompletableFuture.completedFuture(new EmployeeReportResult(null, new ReportError(userInfoResult.userInfoError().getMessage())));
                } else {
                    return loadEmploymentInfo(id).thenCombine(loadSalaryInfo(id),
                        (employeeInfoResult, salaryInfoResult) -> {
                            EmployeeReport employeeReport = constructEmployeeReportFromFields(userInfoResult.userInfo(), employeeInfoResult, salaryInfoResult);
                            return new EmployeeReportResult(employeeReport, null);
                        });
                }
            }
        );
    }

    private CompletableFuture<SalaryInfoResult> loadSalaryInfo(long id) {
        return salaryInfoService.fetchSalaryInfo(id)
            .handle((result, ex) -> {
                if (ex != null) {
                    return new SalaryInfoResult(null, new SalaryInfoException(ex.getMessage()));
                }
                return new SalaryInfoResult(result, null);
            });
    }

    private CompletableFuture<EmploymentInfoResult> loadEmploymentInfo(long id) {
        return employmentInfoService.fetchEmploymentData(id)
            .handle((result, ex) -> {
                if (ex != null) {
                    return new EmploymentInfoResult(null, new EmploymentInfoException(ex.getMessage()));
                }
                return new EmploymentInfoResult(result, null);
            });
    }

    private CompletableFuture<UserInfoResult> loadUserInfo(long id) {
        return userInfoService.fetchBasicInfo(id)
            .handle((result, ex) -> {
                if (ex != null || result == null) {
                    String errorMessage = ex == null ? "UserInfo error" : ex.getMessage();
                    return new UserInfoResult(null, new UserInfoException(errorMessage));
                }
                return new UserInfoResult(result, null);
            });
    }

    private EmployeeReport constructEmployeeReportFromFields(UserInfo userInfo, EmploymentInfoResult employmentInfoResult, SalaryInfoResult salaryInfoResult) {
        String department = null;
        String position = null;
        EmploymentInfo employmentInfo = employmentInfoResult.employmentInfo();
        if (employmentInfo != null) {
            department = employmentInfo.department();
            position = employmentInfo.position();
        }
        BigDecimal salary = null;
        SalaryInfo salaryInfo = salaryInfoResult.salaryInfo();
        if (salaryInfo != null) {
            salary = salaryInfo.salary();
        }
        return new EmployeeReport(userInfo.name(), userInfo.email(),
            department, position, salary, LocalDateTime.now());
    }
}
