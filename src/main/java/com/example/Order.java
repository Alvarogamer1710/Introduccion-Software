package com.example;

import java.util.List;
import java.util.stream.Collectors;

import com.example.service.ExchangeRateService;

public class Order {
    // Atributos
    private String orderId;
    private List<Article> articles;

    private Calculator calculator = new Calculator();
    private ExchangeRateService exchangeRateService = new ExchangeRateService();

    // Constructor
    public Order(String orderId, List<Article> articles) {
        this.orderId = orderId;
        this.articles = articles;
    }

    public Order() {
    }

    // Getters y Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    // Total bruto (sin descuentos)
    public double getGrossTotal() {
        return calculator.calculateTotal(
                articles.stream().map(a -> a.getGrossAmount()).collect(Collectors.toList())
        );
    }

    // Total con descuentos aplicados
    public double getDiscountedTotal() {
        return calculator.calculateTotal(
                articles.stream().map(a -> a.getDiscountedAmount()).collect(Collectors.toList())
        );
    }

    // Métodos para conversión de moneda
    
    /**
     * Obtiene el total bruto en USD.
     */
    public double getGrossTotalUSD() {
        return exchangeRateService.convertEURtoUSD(getGrossTotal());
    }

    /**
     * Obtiene el total con descuentos en USD.
     */
    public double getDiscountedTotalUSD() {
        return exchangeRateService.convertEURtoUSD(getDiscountedTotal());
    }

    /**
     * Obtiene el tipo de cambio actual EUR/USD.
     */
    public double getExchangeRate() {
        return exchangeRateService.getExchangeRate();
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", articles=" + articles +
                ", grossTotal=" + getGrossTotal() +
                ", discountedTotal=" + getDiscountedTotal() +
                '}';

    }
}
