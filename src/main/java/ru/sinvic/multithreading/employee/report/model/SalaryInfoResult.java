package ru.sinvic.multithreading.employee.report.model;

import ru.sinvic.multithreading.employee.report.exceptions.SalaryInfoException;

public record SalaryInfoResult(SalaryInfo salaryInfo, SalaryInfoException salaryInfoException) {
}
