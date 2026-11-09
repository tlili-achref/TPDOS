package com.gfg.microservices.currency_exchange_sample_service;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Exchange_Value")
public class ExchangeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "currency_from")
    private String fromCurrency;

    @Column(name = "currency_to")
    private String toCurrency;

    @Column(name = "conversion_multiple")
    private BigDecimal conversionMultiple;

    @Column(name = "port")
    private int port;

    public ExchangeValue() {
    }

    public ExchangeValue(Long id, String fromCurrency, String toCurrency, BigDecimal conversionMultiple) {
        super();
        this.id = id;
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.conversionMultiple = conversionMultiple;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFrom() { return fromCurrency; }
    public void setFrom(String from) { this.fromCurrency = from; }

    public String getTo() { return toCurrency; }
    public void setTo(String to) { this.toCurrency = to; }

    public BigDecimal getConversionMultiple() { return conversionMultiple; }
    public void setConversionMultiple(BigDecimal conversionMultiple) { this.conversionMultiple = conversionMultiple; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
}