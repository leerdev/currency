package com.leer.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ServiceGiphy {

    FeignClientGiphy feignClientGiphy;

    @Autowired
    public ServiceGiphy(FeignClientGiphy feignClientGiphy) {
        this.feignClientGiphy = feignClientGiphy;
    }

    @Value("${giphy_api_key}")
    private String giphyApiKey;

    /**
     * Returns random gif from giphy.com with given tag
     * @param status - word to look for related gif
     * @return
     */
    public ResponseEntity<Map> getRandomGIF(String status) {
        ResponseEntity<Map> ret = feignClientGiphy.getRndGif(giphyApiKey, status);
        return ret;
    }
}