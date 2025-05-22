package com.example.lab2dao.dao;

import com.example.lab2dao.model.Product;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostgresProductDaoImplIntegrationTest {
    private PostgresProductDaoImpl dao;

    @BeforeAll
    void prepareDatabase() throws SQLException {
        dao = new PostgresProductDaoImpl();

        // Очищаем таблицу перед тестами
        try (var connection = dao.getConnection();
             var statement = connection.createStatement()) {
            statement.execute("DELETE FROM products");
        }
    }

    @Test
    void testCRUDOperations() throws SQLException {
        // Test Create
        Product testProduct = new Product(0, "Test Product", 10, "Test Tag");
        dao.addProduct(testProduct);

        // Test Read
        List<Product> products = dao.getAllProducts();
        assertFalse(products.isEmpty());
        Product savedProduct = products.get(products.size() - 1);
        assertEquals("Test Product", savedProduct.getName());

        // Test Update
        savedProduct.setName("Updated Product");
        savedProduct.setQuantity(20);
        savedProduct.setTag("Updated Tag");
        dao.updateProduct(savedProduct);

        Product updatedProduct = dao.getAllProducts().stream()
                .filter(p -> p.getId() == savedProduct.getId())
                .findFirst()
                .orElseThrow();
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals(20, updatedProduct.getQuantity());
        assertEquals("Updated Tag", updatedProduct.getTag());

        // Test Delete
        int productId = savedProduct.getId();
        dao.deleteProduct(productId);
        assertTrue(dao.getAllProducts().stream()
                .noneMatch(p -> p.getId() == productId));
    }

    @AfterAll
    void cleanDatabase() throws SQLException {
        try (var connection = dao.getConnection();
             var statement = connection.createStatement()) {
            statement.execute("DELETE FROM products WHERE name LIKE 'Test%' OR name LIKE 'Updated%'");
        }
    }
}