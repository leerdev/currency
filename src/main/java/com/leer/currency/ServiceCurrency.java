package com.leer.currency;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ServiceCurrency {

    @Value("${openexchangerates_app_id}")
    private String OER_app_id;
    @Value("${base_currency}")
    private String baseSymbol;

    private final FeignClientOER feignClientOER;

    public ServiceCurrency(FeignClientOER feignClientOER) {
        this.feignClientOER = feignClientOER;
    }

    /**
     * Returns list of active symbols on openexchangerates.org
     * @return
     */
    public List<String> getSymbols() {
        return new ArrayList<>(feignClientOER.getSymbols().keySet());
    }

    /**
     * Returns latest price for a given symbol related to baseSymbol
     * @param cur - currency 3-digit name to look for
     * @return Double price
     */
    public Double getLatestPrice(String cur) {
        priceDto response = feignClientOER.getLatestRate(OER_app_id, cur, baseSymbol);
        Double latestPrice = response.getRates().get(cur);
        return latestPrice;
    }

    /**
     * Returns historical price for a given symbol related to baseSymbol
     * @param cur - currency 3-digit name to look for
     * @param date - date of the historical price
     * @return Double price
     */
    public Double getHistoricalPrice(String cur, String date) {
        priceDto response = feignClientOER.getHistoricalPrice(date, OER_app_id, cur, baseSymbol);
        Double historicalPrice = response.getRates().get(cur);
        return historicalPrice;
    }
}


