package com.example.lab2dao.dao;

import com.example.lab2dao.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDaoMockTest {
    private ProductDao manualMockDao;
    private List<Product> testProducts;
    private Product lastAddedProduct;
    private Product lastUpdatedProduct;
    private Integer lastDeletedId;

    @BeforeEach
    void setUp() {
        lastAddedProduct = null;
        lastUpdatedProduct = null;
        lastDeletedId = null;

        testProducts = Arrays.asList(
                new Product(1, "Ноутбук", 10, "Электроника"),
                new Product(2, "Футболка", 20, "Одежда")
        );

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
        };
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = manualMockDao.getAllProducts();
        assertEquals(2, products.size());
        assertEquals("Ноутбук", products.get(0).getName());
        assertEquals("Футболка", products.get(1).getName());
    }

    @Test
    void testAddProduct() {
        Product newProduct = new Product(3, "Стул", 5, "Мебель");
        manualMockDao.addProduct(newProduct);

        assertNotNull(lastAddedProduct);
        assertEquals("Стул", lastAddedProduct.getName());
        assertEquals(5, lastAddedProduct.getQuantity());
        assertEquals("Мебель", lastAddedProduct.getTag());
    }

    @Test
    void testUpdateProduct() {
        Product updatedProduct = new Product(1, "Ноутбук Pro", 15, "Электроника");
        manualMockDao.updateProduct(updatedProduct);

        assertNotNull(lastUpdatedProduct);
        assertEquals("Ноутбук Pro", lastUpdatedProduct.getName());
        assertEquals(15, lastUpdatedProduct.getQuantity());
    }

    @Test
    void testDeleteProduct() {
        manualMockDao.deleteProduct(1);
        assertNotNull(lastDeletedId);
        assertEquals(1, lastDeletedId);
    }
}