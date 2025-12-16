package ru.sinvic.multithreading.employee.report.model;

import ru.sinvic.multithreading.employee.report.exceptions.EmploymentInfoException;

public record EmploymentInfoResult(EmploymentInfo employmentInfo, EmploymentInfoException employmentInfoException) {
}
