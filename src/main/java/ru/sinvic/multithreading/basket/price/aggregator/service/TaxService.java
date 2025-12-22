package ru.sinvic.multithreading.basket.price.aggregator.service;

public interface TaxService {
    Double calculateTax(Double subtotal) throws Exception;
}
