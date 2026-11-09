package com.gfg.microservices.currency_exchange_sample_service;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<ExchangeValue, Long> {

    Optional<ExchangeValue> findByFromCurrencyIgnoreCaseAndToCurrencyIgnoreCase(String fromCurrency,
                                                                               String toCurrency);
}