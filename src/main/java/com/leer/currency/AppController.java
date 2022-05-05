package com.leer.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@RestController()
@RequestMapping("/currency")
public class AppController {

    private final ServiceGiphy serviceGiphy;
    private final ServiceCurrency serviceCurrency;

    @Autowired
    public AppController(ServiceGiphy serviceGiphy, ServiceCurrency serviceCurrency) {
        this.serviceGiphy = serviceGiphy;
        this.serviceCurrency = serviceCurrency;
    }

    @Value("${rich_keyword}")
    private String RICH;
    @Value("${broke_keyword}")
    private String BROKE;
    @Value("${error_keyword}")
    private String ERROR;
    @Value("${boring_keyword}")
    private String BORING;

    /**
     * Returns a gif-file according to the rate of given currency to USD related to
     * yesterday price:
     * 'broke' gif - in case todays' rate is lower, 'rich' gif if higher,
     * 'error' gif if currency or any rate is not found, 'boring' gif if the price is same.
     * Base currency is USD.
     * @param cur - Currency name
     * @return gif-file
     */
    @GetMapping(path = "/gif/{cur}")
    @CrossOrigin(origins = "http://localhost:8080")
    ResponseEntity<Map> currencyGipher(@PathVariable String cur) {

        ResponseEntity<Map> ret = null;

        // Check if the currency is in active symbols/exists
        List symbols = serviceCurrency.getSymbols();

        // return error gif if currency is not found
        if (!symbols.contains(cur)) {
            ret = serviceGiphy.getRandomGIF(ERROR);
            ret.getBody().put("my_app_message", "Cannot find symbol " + cur);
            return ret;
        }

        // getting prices latest and historical prices
        Double latestPrice = serviceCurrency.getLatestPrice(cur);
        Double historicalPrice = serviceCurrency.getHistoricalPrice(cur, getYesterdayDate());
        // getting status of the today's' price comparing to yesterday's
        int status = getStatus(latestPrice, historicalPrice);
        // checking the status and choose the key word for gif search
        switch (status) {
            case 1:
                ret = serviceGiphy.getRandomGIF(RICH); // current price is bigger than yesterdays'
                ret.getBody().put("my_app_message", "price got higher");
                break;
            case -1:
                ret = serviceGiphy.getRandomGIF(BROKE); // current price is lower
                ret.getBody().put("my_app_message", "price got lower");
                break;
            case 0:
                ret = serviceGiphy.getRandomGIF(BORING); // The price has not changed
                ret.getBody().put("my_app_message", "price is not changed, boring!");
                break;
            case -1000:
                ret = serviceGiphy.getRandomGIF(ERROR); // An error occurs
                ret.getBody().put("my_app_message", "price search error!");
                break;
        }

        // adding some additional information to the response
        ret.getBody().put("latest_price", latestPrice);
        ret.getBody().put("historical_price", historicalPrice);
        return ret;
    }

    /**
     * Returns an active currency symbols from openexchangerates.org
     * @return list of strings
     */
    @GetMapping("/get_symbols")
    @CrossOrigin(origins = "http://localhost:8080")
    public List<String> getSymbols() {
        return serviceCurrency.getSymbols();
    }

    /**
     * Returns a date of yesterday date
     * @return String in YYYY-MM-DD format
     */
    private String getYesterdayDate() {
        LocalDate date = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    /**
     * Returns a code of price comparison, or -100 if any price is not available
     * @param latestPrice - latest available price
     * @param historicalPrice - price of a day ago
     * @return (int) 1 - if current price is bigger than yesterdays, -1 - if lower,
     *              0 if the price is the same, -1000 in case of an error
     */
    private int getStatus(Double latestPrice, Double historicalPrice) {
        if (latestPrice == null || historicalPrice == null)
            return -1000;
        if (latestPrice > historicalPrice)
            return 1;
        else if (latestPrice < historicalPrice)
            return -1;
        return 0;
    }

}
