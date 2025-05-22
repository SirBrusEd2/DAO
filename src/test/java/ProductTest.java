package com.example.lab2dao.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductCreation() {
        Product product = new Product(1, "Test Product", 10, "Test Tag");

        assertEquals(1, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals(10, product.getQuantity());
        assertEquals("Test Tag", product.getTag());
    }

    @Test
    void testSetters() {
        Product product = new Product(0, "", 0, "");

        product.setId(5);
        product.setName("New Name");
        product.setQuantity(15);
        product.setTag("New Tag");

        assertEquals(5, product.getId());
        assertEquals("New Name", product.getName());
        assertEquals(15, product.getQuantity());
        assertEquals("New Tag", product.getTag());
    }

    @Test
    void testEqualsAndHashCode() {
        Product product1 = new Product(1, "Product", 10, "Tag");
        Product product2 = new Product(1, "Product", 10, "Tag");
        Product product3 = new Product(2, "Another", 20, "Tag");

        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1, product3);
    }

    @Test
    void testToString() {
        Product product = new Product(1, "Test", 5, "Tag");
        String expected = "Product{id=1, name='Test', quantity=5, tag='Tag'}";

        assertEquals(expected, product.toString());
    }
}