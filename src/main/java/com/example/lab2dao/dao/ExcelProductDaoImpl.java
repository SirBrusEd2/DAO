/**
 * Реализация интерфейса ProductDao для работы с данными продуктов в Excel-файлах.
 * Использует библиотеку Apache POI для работы с форматом XLSX.
 *
 * <p>Особенности реализации:
 * <ul>
 *     <li>По умолчанию работает с файлом products.xlsx</li>
 *     <li>Автоматически генерирует ID для новых записей</li>
 *     <li>Создает новый файл при первом сохранении, если он не существует</li>
 *     <li>Хранит данные в первом листе файла с заданной структурой колонок</li>
 * </ul>
 *
 * @see ProductDao
 */
package com.example.lab2dao.dao;

import com.example.lab2dao.model.Product;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelProductDaoImpl implements ProductDao {
    private String filePath = "products.xlsx";

    /**
     * Устанавливает путь к Excel-файлу для работы с данными.
     * @param source путь к файлу .xlsx
     */
    @Override
    public void setDataSource(String source) {
        this.filePath = source;
    }

    /**
     * Добавляет новый продукт в файл с автоматической генерацией ID.
     * ID генерируется как максимальный существующий ID + 1.
     * @param product добавляемый продукт (ID игнорируется и заменяется новым)
     */
    @Override
    public void addProduct(Product product) {
        List<Product> products = getAllProducts();
        int newId = products.stream().mapToInt(Product::getId).max().orElse(0) + 1;
        product.setId(newId);
        products.add(product);
        saveAllProducts(products);
    }

    /**
     * Обновляет данные существующего продукта в файле.
     * @param product продукт с обновленными данными (ищется по ID)
     */
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

    /**
     * Удаляет продукт по указанному ID из файла.
     * @param id идентификатор удаляемого продукта
     */
    @Override
    public void deleteProduct(int id) {
        List<Product> products = getAllProducts();
        products.removeIf(p -> p.getId() == id);
        saveAllProducts(products);
    }

    /**
     * Получает список всех продуктов из Excel-файла.
     * @return список продуктов. Если файл не существует, возвращает пустой список.
     * @throws RuntimeException при ошибках чтения файла (кроме случая отсутствия файла)
     */
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
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла: " + filePath, e);
        }
        return products;
    }

    /**
     * Внутренний метод для сохранения списка продуктов в файл.
     * Создает новый файл с заголовками, если он не существует.
     * @param products список продуктов для сохранения
     */
    private void saveAllProducts(List<Product> products) {
        try (Workbook workbook = new XSSFWorkbook();
             OutputStream os = new FileOutputStream(filePath)) {

            Sheet sheet = workbook.createSheet("Products");

            // Создание шапки таблицы
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Quantity");
            headerRow.createCell(3).setCellValue("Tag");

            // Заполнение данных
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
            throw new RuntimeException("Ошибка записи в файл: " + filePath, e);
        }
    }
}