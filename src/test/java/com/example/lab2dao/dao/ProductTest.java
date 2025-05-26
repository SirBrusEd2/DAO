package com.example.lab2dao.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testIsLeapYear() {
        // Создаем тестовый продукт (параметры не важны для этого теста)
        Product product = new Product(1, "Test Product", 10, "Test Category");

        // Стандартные невисокосные годы
        assertFalse(product.isLeapYear(2025));
        assertFalse(product.isLeapYear(2023));
        assertFalse(product.isLeapYear(1999));
        assertFalse(product.isLeapYear(1900)); // Делится на 100, но не на 400

        // Стандартные високосные годы
        assertTrue(product.isLeapYear(2024));
        assertTrue(product.isLeapYear(2020));
        assertTrue(product.isLeapYear(2004));

        // Граничные случаи
        assertTrue(product.isLeapYear(2000)); // Делится на 400
        assertFalse(product.isLeapYear(100)); // Делится на 100, но не на 400
        assertTrue(product.isLeapYear(4));    // Делится на 4

        // Отрицательные года (если метод должен их обрабатывать)
        assertFalse(product.isLeapYear(-1));
        assertTrue(product.isLeapYear(-4));  // Отрицательный високосный
    }
}