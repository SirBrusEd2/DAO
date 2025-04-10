package com.example.lab2dao;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelProductDaoImpl implements ProductDao {
    private String filePath = "products.xlsx";

    @Override
    public void setDataSource(String source) {
        this.filePath = source;
    }

    @Override
    public void addProduct(Product product) {
        List<Product> products = getAllProducts();
        int newId = products.stream().mapToInt(Product::getId).max().orElse(0) + 1;
        product.setId(newId);
        products.add(product);
        saveAllProducts(products);
    }

    @Override
    public void updateProduct(Product product) {
        List<Product> products = getAllProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == product.getId()) {
                products.set(i, product);
                break;
            }
        }
        saveAllProducts(products);
    }

    @Override
    public void deleteProduct(int id) {
        List<Product> products = getAllProducts();
        products.removeIf(p -> p.getId() == id);
        saveAllProducts(products);
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();

        try (InputStream is = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Пропускаем заголовок

                int id = (int) row.getCell(0).getNumericCellValue();
                String name = row.getCell(1).getStringCellValue();
                int quantity = (int) row.getCell(2).getNumericCellValue();
                String tag = row.getCell(3).getStringCellValue();

                products.add(new Product(id, name, quantity, tag));
            }
        } catch (IOException e) {
            // Если файл не существует, вернем пустой список
            return new ArrayList<>();
        }
        return products;
    }

    private void saveAllProducts(List<Product> products) {
        try (Workbook workbook = new XSSFWorkbook();
             OutputStream os = new FileOutputStream(filePath)) {

            Sheet sheet = workbook.createSheet("Products");

            // Создаем заголовок
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Quantity");
            headerRow.createCell(3).setCellValue("Tag");

            // Заполняем данные
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(p.getId());
                row.createCell(1).setCellValue(p.getName());
                row.createCell(2).setCellValue(p.getQuantity());
                row.createCell(3).setCellValue(p.getTag());
            }

            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}