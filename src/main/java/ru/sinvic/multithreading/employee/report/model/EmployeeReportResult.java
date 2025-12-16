package ru.sinvic.multithreading.employee.report.model;

import ru.sinvic.multithreading.employee.report.exceptions.ReportError;

public record EmployeeReportResult(EmployeeReport employeeReport, ReportError reportError) {
}
