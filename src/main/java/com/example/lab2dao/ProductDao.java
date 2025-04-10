package com.example.lab2dao;

import java.util.List;

public interface ProductDao {
    void addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(int id);
    List<Product> getAllProducts();
    void setDataSource(String source); // Новый метод для выбора источника данных
}