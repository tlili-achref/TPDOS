package com.gfg.microservices.currency_exchange_sample_service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeSampleController {

    @Autowired
    private Environment environment;

    @Autowired
    private ExchangeService exchangeService;

    // 1) Version du TP : logique codée en dur
    @GetMapping("/currency-exchange-sample/fromCurrency/{fromCurrency}/toCurrency/{toCurrency}")
    public ExchangeValue retrieveExchangeValue(@PathVariable String fromCurrency,
                                               @PathVariable String toCurrency) {
        BigDecimal conversionMultiple = null;

        if (fromCurrency != null && toCurrency != null) {
            if (fromCurrency.equalsIgnoreCase("USD") && toCurrency.equalsIgnoreCase("INR")) {
                conversionMultiple = BigDecimal.valueOf(78);
            }
            if (fromCurrency.equalsIgnoreCase("INR") && toCurrency.equalsIgnoreCase("USD")) {
                conversionMultiple = BigDecimal.valueOf(0.013);
            }
            if (fromCurrency.equalsIgnoreCase("EUR") && toCurrency.equalsIgnoreCase("INR")) {
                conversionMultiple = BigDecimal.valueOf(82);
            }
            if (fromCurrency.equalsIgnoreCase("AUD") && toCurrency.equalsIgnoreCase("INR")) {
                conversionMultiple = BigDecimal.valueOf(54);
            }
        }

        ExchangeValue exchangeValue = new ExchangeValue(1L, fromCurrency, toCurrency, conversionMultiple);
        // setting the port
        exchangeValue.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
        return exchangeValue;
    }

    // 2) Lecture depuis la base H2
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public ResponseEntity<ExchangeValue> retrieveFromDb(@PathVariable String from, @PathVariable String to) {
        return exchangeService.getByFromTo(from, to)
                .map(ev -> {
                    ev.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
                    return ResponseEntity.ok(ev);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 3) Liste complète
    @GetMapping("/currency-exchange")
    public List<ExchangeValue> getAll() {
        return exchangeService.getAll();
    }

    // 4) Création
    @PostMapping("/currency-exchange")
    public ResponseEntity<ExchangeValue> create(@RequestBody ExchangeValue ex) {
        ex.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
        return ResponseEntity.status(HttpStatus.CREATED).body(exchangeService.createExchangeValue(ex));
    }
}