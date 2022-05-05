# Currency Gifer

## Description
Returns a gif-file according to the rate of given currency to USD related to yesterday's price.

## Deploy and access
Jar file located in \out\artifacts\currency_jar and may be run by command line "java -jar currency.jar".
Tiny http client is in \out\production\resources\static and \src\main\resources\static
Server is at localhost:8080

## API
Gives list of active currencies (symbols) at openexchangerates.org
`GET /currency/get_symbols`

Returns a gif-file according to the rate of given currency to USD related to yesterday price: 'broke' gif - in case todays' rate is lower, 'rich' gif if higher, 'error' gif if currency or any rate is not found, 'boring' gif if the price is same. Base currency is USD.
`Get /gif/{cur}`

