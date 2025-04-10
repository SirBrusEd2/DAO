package com.example.lab2dao;

import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    private final List<Product> products = new ArrayList<>();
    private int nextId = 1;

    @Override
    public void addProduct(Product product) {
        product.setId(nextId++);
        products.add(product);
    }

    @Override
    public void updateProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == product.getId()) {
                products.set(i, product);
                break;
            }
        }
    }

    @Override
    public void deleteProduct(int id) {
        products.removeIf(product -> product.getId() == id);
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    @Override
    public void setDataSource(String source) {
        // Для in-memory реализации не требуется дополнительных действий
    }
}