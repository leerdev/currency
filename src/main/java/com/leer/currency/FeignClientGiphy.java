package com.leer.currency;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Client to communicate to giphy.com API
 */
@FeignClient(name = "FeignClientGiphy", url = "${giphy_api_endpoint}")
public interface FeignClientGiphy {
    @GetMapping("/random")
    ResponseEntity<Map> getRndGif(
            @RequestParam("api_key") String apiKey,
            @RequestParam("tag") String tag
    );
}
