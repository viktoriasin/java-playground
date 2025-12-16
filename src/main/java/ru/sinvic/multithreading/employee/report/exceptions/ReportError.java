package ru.sinvic.multithreading.employee.report.exceptions;

public class ReportError extends RuntimeException {
    public ReportError(String message) {
        super(message);
    }
}
