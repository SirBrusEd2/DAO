package com.example.lab2dao.dao;

import com.example.lab2dao.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDaoMockTest {

    @Mock
    private ProductDao mockDao;

    @Test
    void testGetAllProducts() {
        // Подготовка тестовых данных
        Product product1 = new Product(1, "Product 1", 10, "Tag 1");
        Product product2 = new Product(2, "Product 2", 20, "Tag 2");
        List<Product> testProducts = Arrays.asList(product1, product2);

        // Настройка мока
        when(mockDao.getAllProducts()).thenReturn(testProducts);

        // Вызов тестируемого метода
        List<Product> result = mockDao.getAllProducts();

        // Проверки
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals(20, result.get(1).getQuantity());

        // Проверка вызова метода
        verify(mockDao, times(1)).getAllProducts();
    }

    @Test
    void testAddProduct() {
        Product newProduct = new Product(0, "New Product", 5, "New Tag");

        // Настройка мока
        doNothing().when(mockDao).addProduct(any(Product.class));

        // Вызов тестируемого метода
        mockDao.addProduct(newProduct);

        // Проверка вызова метода
        verify(mockDao, times(1)).addProduct(newProduct);
    }

    @Test
    void testUpdateProduct() {
        Product existingProduct = new Product(1, "Existing", 10, "Tag");

        // Настройка мока
        doNothing().when(mockDao).updateProduct(any(Product.class));

        // Вызов тестируемого метода
        mockDao.updateProduct(existingProduct);

        // Проверка вызова метода
        verify(mockDao, times(1)).updateProduct(existingProduct);
    }
}