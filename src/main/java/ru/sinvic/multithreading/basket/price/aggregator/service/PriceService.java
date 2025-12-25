package ru.sinvic.multithreading.basket.price.aggregator.service;

import ru.sinvic.multithreading.basket.price.aggregator.dto.PriceInfo;

public interface PriceService {
    PriceInfo getPriceInfo(long productId) throws Exception;
}
