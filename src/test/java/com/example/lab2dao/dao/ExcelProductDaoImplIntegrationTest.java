package com.example.lab2dao.dao;

import com.example.lab2dao.model.Product;
import org.junit.jupiter.api.*;
import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ExcelProductDaoImplIntegrationTest {
    private ExcelProductDaoImpl dao;
    private final String testFilePath = "test_products.xlsx";

    @BeforeEach
    void setup() {
        // Удаляем старый файл и создаем новый DAO перед КАЖДЫМ тестом
        new File(testFilePath).delete();
        dao = new ExcelProductDaoImpl(testFilePath); // Используем конструктор с параметром
    }

    @Test
    void testAddAndGetProduct() {
        Product product = new Product(0, "Test Chair", 50, "Furniture");
        dao.addProduct(product);

        List<Product> products = dao.getAllProducts();
        assertFalse(products.isEmpty());
        assertEquals("Test Chair", products.get(0).getName());
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product(0, "Test Desk", 30, "Furniture");
        dao.addProduct(product);
        product.setQuantity(25);

        dao.updateProduct(product);
        List<Product> products = dao.getAllProducts();
        assertEquals(25, products.get(0).getQuantity());
    }

    @Test
    void testDeleteProduct() {
        Product product = new Product(0, "Test Lamp", 10, "Lighting");
        dao.addProduct(product);
        int id = product.getId();

        dao.deleteProduct(id);
        List<Product> products = dao.getAllProducts();
        assertTrue(products.stream().noneMatch(p -> p.getId() == id));
    }

    @AfterAll
    static void cleanup() {
        // Удаляем тестовый файл после всех тестов (опционально)
        new File("test_products.xlsx").delete();
    }
}