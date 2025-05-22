package com.example.lab2dao.dao;

import com.example.lab2dao.model.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExcelProductDaoImplTest {
    private ExcelProductDaoImpl excelDao;
    private final String TEST_FILE = "test_products.xlsx";

    @BeforeEach
    void setUp() {
        excelDao = new ExcelProductDaoImpl();
        excelDao.setDataSource(TEST_FILE);
    }

    @Test
    void testExcelOperations() {
        // Test Create
        Product product1 = new Product(0, "Excel Product 1", 5, "Excel Tag");
        Product product2 = new Product(0, "Excel Product 2", 10, "Excel Tag");

        excelDao.addProduct(product1);
        excelDao.addProduct(product2);

        // Test Read
        List<Product> products = excelDao.getAllProducts();
        assertEquals(2, products.size());
        assertEquals("Excel Product 1", products.get(0).getName());

        // Test Update
        Product toUpdate = products.get(0);
        toUpdate.setName("Updated Excel Product");
        excelDao.updateProduct(toUpdate);

        List<Product> updatedProducts = excelDao.getAllProducts();
        assertEquals("Updated Excel Product",
                updatedProducts.stream()
                        .filter(p -> p.getId() == toUpdate.getId())
                        .findFirst()
                        .get()
                        .getName());

        // Test Delete
        int idToDelete = updatedProducts.get(1).getId();
        excelDao.deleteProduct(idToDelete);
        assertEquals(1, excelDao.getAllProducts().size());
    }

    @AfterEach
    void tearDown() {
        // Удаляем тестовый файл после каждого теста
        new File(TEST_FILE).delete();
    }
}