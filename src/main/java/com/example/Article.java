package com.example;

public class Article {
    // Atributos
    private String name;
    private int quantity;
    private double price;
    private double discount; // en porcentaje (%)

    private Calculator calculator = new Calculator();

    // Constructor
    public Article(String name, int quantity, double price, double discount) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    // Devuelve el total bruto (cantidad * precio)
    public double getGrossAmount() {
        return calculator.multiply(quantity, (int) price);
    }

    // Devuelve el total con descuento aplicado
    public double getDiscountedAmount() {
        return calculator.discount(getGrossAmount(), discount);
    }

    @Override
    public String toString() {
        return "Article{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", discount=" + discount +
                ", grossAmount=" + getGrossAmount() +
                ", discountedAmount=" + getDiscountedAmount() +
                '}';
    }
}
