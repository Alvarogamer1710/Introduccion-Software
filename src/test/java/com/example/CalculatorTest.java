package com.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Arrays;

class CalculatorTest {

    @Test
    void testMultiply() {
        Calculator calc = new Calculator();
        assertEquals(20, calc.multiply(4, 5));
    }

    @Test
    void testConcat_validStrings() {
        Calculator calc = new Calculator();
        assertEquals("HolaMundo", calc.concat("Hola", "Mundo"));
    }

    @Test
    void testConcat_withNull() {
        Calculator calc = new Calculator();
        assertEquals(Calculator.EMPTY, calc.concat("Hola", null));
        assertEquals(Calculator.EMPTY, calc.concat(null, "Mundo"));
        assertEquals(Calculator.EMPTY, calc.concat(null, null));
    }

    @Test
    void testSum() {
        Calculator calc = new Calculator();
        assertEquals(7.5, calc.sum(3.0, 4.5), 0.0001);
    }

    @Test
    void testDiscount_valid() {
        Calculator calc = new Calculator();
        assertEquals(80.0, calc.discount(100, 20), 0.0001);
    }

    @Test
    void testDiscount_invalidPercent() {
        Calculator calc = new Calculator();
        assertThrows(IllegalArgumentException.class, () -> calc.discount(100, -5));
        assertThrows(IllegalArgumentException.class, () -> calc.discount(100, 150));
    }

    @Test
    void testCalculateTotal() {
        Calculator calc = new Calculator();
        List<Double> amounts = Arrays.asList(10.0, 20.0, 30.0);
        assertEquals(60.0, calc.calculateTotal(amounts), 0.0001);
    }

    @Test
    void testCalculateTotal_emptyList() {
        Calculator calc = new Calculator();
        List<Double> amounts = Arrays.asList();
        assertEquals(0.0, calc.calculateTotal(amounts), 0.0001);
    }
}
