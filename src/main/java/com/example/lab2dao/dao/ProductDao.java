/**
 * Интерфейс доступа к данным для работы с продуктами (DAO-паттерн).
 * Определяет базовые CRUD-операции и управление источниками данных.
 *
 * <p>Основные функции:
 * <ul>
 *     <li>Абстрагирование доступа к данным</li>
 *     <li>Единый интерфейс для различных реализаций (БД, Excel, память)</li>
 *     <li>Поддержка изменения источника данных во время выполнения</li>
 * </ul>
 *
 * <p>Реализации должны обеспечивать:
 * <ul>
 *     <li>Корректную генерацию ID при добавлении новых записей</li>
 *     <li>Потокобезопасность операций (если требуется)</li>
 *     <li>Обработку ошибок ввода/вывода</li>
 * </ul>
 *
 * @see ProductDaoImpl
 * @see ExcelProductDaoImpl
 * @see PostgresProductDaoImpl
 */
package com.example.lab2dao.dao;

import com.example.lab2dao.model.Product;

import java.util.List;

public interface ProductDao {
    /**
     * Добавляет новый продукт в хранилище данных.
     * @param product добавляемый объект продукта (ID может игнорироваться)
     * @throws IllegalArgumentException если продукт не соответствует бизнес-правилам
     */
    void addProduct(Product product);

    /**
     * Обновляет данные существующего продукта.
     * @param product объект продукта с обновленными данными (должен содержать валидный ID)
     * @throws IllegalArgumentException если продукт не найден или данные невалидны
     */
    void updateProduct(Product product);

    /**
     * Удаляет продукт по идентификатору.
     * @param id уникальный идентификатор удаляемого продукта
     * @throws IllegalArgumentException если продукт с указанным ID не существует
     */
    void deleteProduct(int id);

    /**
     * Получает список всех доступных продуктов.
     * @return список продуктов. Пустой список если данные отсутствуют
     */
    List<Product> getAllProducts();

    /**
     * Устанавливает источник данных для реализации DAO.
     * @param source специфичный для реализации идентификатор источника данных
     *              (путь к файлу, URL БД, имя ресурса и т.д.)
     */
    void setDataSource(String source);
}