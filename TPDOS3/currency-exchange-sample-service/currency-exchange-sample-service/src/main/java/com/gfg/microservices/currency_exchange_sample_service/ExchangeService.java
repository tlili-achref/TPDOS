package com.gfg.microservices.currency_exchange_sample_service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeService {

    @Autowired
    ExchangeRepository exchangeRepository;

    // CREATE
    public ExchangeValue createExchangeValue(ExchangeValue ex) {
        return exchangeRepository.save(ex);
    }

    // READ ALL
    public List<ExchangeValue> getAll() {
        return exchangeRepository.findAll();
    }

    // READ ONE (from / to)
    public Optional<ExchangeValue> getByFromTo(String from, String to) {
        return exchangeRepository.findByFromCurrencyIgnoreCaseAndToCurrencyIgnoreCase(from, to);
    }
}