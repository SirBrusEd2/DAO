package com.example.lab2dao.dao;

import com.example.lab2dao.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoMockTest {

    // Вместо @Mock создаем ручную реализацию
    private ProductDao manualMockDao;
    private List<Product> testProducts;
    private Product lastAddedProduct;
    private Product lastUpdatedProduct;
    private Integer lastDeletedId;

    @BeforeEach
    void setUp() {
        // Сбрасываем состояние перед каждым тестом
        lastAddedProduct = null;
        lastUpdatedProduct = null;
        lastDeletedId = null;

        // Подготовка тестовых данных
        testProducts = Arrays.asList(
                new Product(1, "Ноутбук", 10, "Электроника"),
                new Product(2, "Футболка", 20, "Одежда")
        );

        // Создаем ручную mock-реализацию интерфейса
        manualMockDao = new ProductDao() {
            @Override
            public List<Product> getAllProducts() {
                return testProducts;
            }

            @Override
            public void addProduct(Product product) {
                lastAddedProduct = product;
            }

            @Override
            public void updateProduct(Product product) {
                lastUpdatedProduct = product;
            }

            @Override
            public void deleteProduct(int id) {
                lastDeletedId = id;
            }

            @Override
            public void setDataSource(String source) {
                // Пустая реализация для mock
            }
        };
    }

    @Test
    void testGetAllProducts() {
        // Вызов метода и проверка
        List<Product> products = manualMockDao.getAllProducts();

        assertEquals(2, products.size());
        assertEquals("Ноутбук", products.get(0).getName());
        assertEquals("Футболка", products.get(1).getName());
    }

    @Test
    void testAddProduct() {
        // Подготовка тестовых данных
        Product newProduct = new Product(3, "Стул", 5, "Мебель");

        // Вызов метода
        manualMockDao.addProduct(newProduct);

        // Проверка, что продукт был "добавлен"
        assertNotNull(lastAddedProduct);
        assertEquals("Стул", lastAddedProduct.getName());
        assertEquals(5, lastAddedProduct.getQuantity());
        assertEquals("Мебель", lastAddedProduct.getTag());
    }

    @Test
    void testUpdateProduct() {
        // Подготовка тестовых данных
        Product updatedProduct = new Product(1, "Ноутбук Pro", 15, "Электроника");

        // Вызов метода
        manualMockDao.updateProduct(updatedProduct);

        // Проверка, что продукт был "обновлен"
        assertNotNull(lastUpdatedProduct);
        assertEquals("Ноутбук Pro", lastUpdatedProduct.getName());
        assertEquals(15, lastUpdatedProduct.getQuantity());
    }

    @Test
    void testDeleteProduct() {
        // Вызов метода
        manualMockDao.deleteProduct(1);

        // Проверка, что продукт был "удален"
        assertNotNull(lastDeletedId);
        assertEquals(1, lastDeletedId);
    }
}