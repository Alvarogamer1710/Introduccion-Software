package com.example;

import java.util.List;
import java.util.stream.Collectors;

public class Order {
    // Atributos
    private String orderId;
    private List<Article> articles;

    private Calculator calculator = new Calculator();

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
