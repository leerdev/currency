package com.leer.currency;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Client to communicate to openexchangerates.org API
 */
@FeignClient(name = "FeignClientOER", url = "${currency_endpoint}")
public interface FeignClientOER {

    @GetMapping("/currencies.json")
    Map<String, String> getSymbols(
    );

    @GetMapping("/latest.json")
    priceDto getLatestRate(
            @RequestParam("app_id") String appId,
            @RequestParam("symbols") String symbol,
            @RequestParam("base") String baseSymbol
    );

    @GetMapping("/historical/{date}.json")
    priceDto getHistoricalPrice(
            @PathVariable String date,
            @RequestParam("app_id") String appId,
            @RequestParam("symbols") String symbol,
            @RequestParam("base") String baseSymbol
    );
}
