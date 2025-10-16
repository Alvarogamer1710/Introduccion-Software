package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestArticle {

    @Test
    public void testGetGrossAmount() {
        Article article = new Article("Laptop", 2, 500.0, 10.0);
        double expected = 1000.0;
        double result = article.getGrossAmount();
        assertEquals(expected, result, 0.001, "El total bruto debe ser cantidad * precio");
    }

    @Test
    public void testGetDiscountedAmount() {
        Article article = new Article("Laptop", 2, 500.0, 10.0);
        double expected = 900.0; // 10% descuento de 1000
        double result = article.getDiscountedAmount();
        assertEquals(expected, result, 0.001, "El total con descuento debe aplicarse correctamente");
    }

    @Test
    public void testZeroDiscount() {
        Article article = new Article("Mouse", 2, 50.0, 0.0);
        double expected = 100.0; // sin descuento
        double result = article.getDiscountedAmount();
        assertEquals(expected, result, 0.001, "Si el descuento es 0, el total no debe cambiar");
    }

    @Test
    public void testFullDiscount() {
        Article article = new Article("Gift", 1, 200.0, 100.0);
        double expected = 0.0; // descuento 100%
        double result = article.getDiscountedAmount();
        assertEquals(expected, result, 0.001, "El descuento del 100% debe dar total 0");
    }
}
