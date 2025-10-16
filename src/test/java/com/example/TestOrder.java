package com.example;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class TestOrder {

    @Test
    public void testGetGrossTotal() {
        Article a1 = new Article("Laptop", 1, 1000.0, 10.0);
        Article a2 = new Article("Mouse", 2, 50.0, 0.0);
        Article a3 = new Article("Teclado", 1, 80.0, 20.0);

        Order order = new Order("ORD123", Arrays.asList(a1, a2, a3));

        double expected = 1180.0;
        double result = order.getGrossTotal();

        assertEquals(expected, result, 0.001, "El total bruto debe ser la suma de los importes brutos");
    }

    @Test
    public void testGetDiscountedTotal() {
        Article a1 = new Article("Laptop", 1, 1000.0, 10.0); // 900
        Article a2 = new Article("Mouse", 2, 50.0, 0.0);      // 100
        Article a3 = new Article("Teclado", 1, 80.0, 20.0);   // 64

        Order order = new Order("ORD124", Arrays.asList(a1, a2, a3));

        double expected = 1064.0;
        double result = order.getDiscountedTotal();

        assertEquals(expected, result, 0.001, "El total con descuento debe sumar los importes descontados");
    }

    @Test
    public void testEmptyOrder() {
        Order order = new Order("ORD_EMPTY", Collections.emptyList());
        assertEquals(0.0, order.getGrossTotal(), 0.001, "Un pedido vacío debe tener total 0");
        assertEquals(0.0, order.getDiscountedTotal(), 0.001, "Un pedido vacío debe tener total descontado 0");
    }

    @Test
    public void testGettersAndSetters() {
        Order order = new Order("ORD001", Collections.emptyList());

        // Test setOrderId y getOrderId
        order.setOrderId("ORD_CHANGED");
        assertEquals("ORD_CHANGED", order.getOrderId());

        // Test setArticles y getArticles
        Article a1 = new Article("Mouse", 2, 50.0, 0.0);
        order.setArticles(Arrays.asList(a1));
        assertEquals(1, order.getArticles().size());
    }

    @Test
    public void testToString() {
        Article a1 = new Article("Monitor", 1, 200.0, 10.0);
        Order order = new Order("ORD999", Arrays.asList(a1));

        String output = order.toString();
        assertTrue(output.contains("ORD999"));
        assertTrue(output.contains("grossTotal"));
        assertTrue(output.contains("discountedTotal"));
    }

    @Test
    public void testNullArticlesList() {
        // Cubre caso donde articles = null
        Order order = new Order("ORD_NULL", null);
        assertThrows(NullPointerException.class, order::getGrossTotal,
                "Si la lista es nula, debe lanzar NullPointerException");
    }
}
