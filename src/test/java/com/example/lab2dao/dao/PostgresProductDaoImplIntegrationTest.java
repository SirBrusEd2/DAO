package com.example.lab2dao.dao;

import com.example.lab2dao.model.Product;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostgresProductDaoImplIntegrationTest {
    private PostgresProductDaoImpl dao;
    private Connection connection;

    @BeforeAll
    void setup() throws SQLException {
        dao = new PostgresProductDaoImpl();
        connection = dao.getConnection(); // Убедитесь, что метод getConnection() существует
    }

    @BeforeEach
    void clearDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM products"); // Очистка перед КАЖДЫМ тестом
        }
    }

    @Test
    void testAddAndGetProduct() {
        Product product = new Product(0, "Test Laptop", 10, "Electronics");
        dao.addProduct(product);

        List<Product> products = dao.getAllProducts();
        assertFalse(products.isEmpty());
        assertEquals("Test Laptop", products.get(0).getName()); // Теперь будет корректно
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product(0, "Test Phone", 5, "Electronics");
        dao.addProduct(product);
        product.setQuantity(15);

        dao.updateProduct(product);
        List<Product> products = dao.getAllProducts();
        assertEquals(15, products.get(0).getQuantity());
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product(0, "Test Book", 20, "Books");
        dao.addProduct(product);
        int id = product.getId();

        dao.deleteProduct(id);
        List<Product> products = dao.getAllProducts();
        assertTrue(products.stream().noneMatch(p -> p.getId() == id));
    }

    @AfterAll
    void cleanup() throws SQLException {
        connection.close(); // Закрываем соединение после всех тестов
    }
}