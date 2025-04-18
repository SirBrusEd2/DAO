/**
 * Класс-модель для представления товара с использованием JavaFX-свойств.
 * Обеспечивает поддержку наблюдения за изменениями данных и биндинга с UI-компонентами.
 *
 * <p>Структура данных:
 * <ul>
 *     <li>id - уникальный идентификатор товара</li>
 *     <li>name - наименование товара</li>
 *     <li>quantity - количество на складе</li>
 *     <li>tag - дополнительный тег/категория товара</li>
 * </ul>
 *
 * <p>Особенности реализации:
 * <ul>
 *     <li>Использует JavaFX properties для совместимости с TableView и другими компонентами</li>
 *     <li>Поддерживает двусторонний биндинг данных</li>
 *     <li>Автоматически уведомляет об изменениях в данных</li>
 * </ul>
 */
package com.example.lab2dao.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {
    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty quantity;
    private final StringProperty tag;

    /**
     * Создает новый экземпляр товара с заданными параметрами.
     *
     * @param id уникальный идентификатор
     * @param name название товара
     * @param quantity количество товара
     * @param tag категория/метка товара
     */
    public Product(int id, String name, int quantity, String tag) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.tag = new SimpleStringProperty(tag);
    }

    /**
     * Возвращает числовое значение идентификатора товара.
     * @return текущий ID
     */
    public int getId() {
        return id.get();
    }

    /**
     * Предоставляет свойство для биндинга ID товара.
     * @return IntegerProperty с возможностью наблюдения изменений
     */
    public IntegerProperty idProperty() {
        return id;
    }

    /**
     * Устанавливает новое значение идентификатора товара.
     * @param id новый ID
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Возвращает текстовое значение названия товара.
     * @return текущее название
     */
    public String getName() {
        return name.get();
    }

    /**
     * Предоставляет свойство для биндинга названия товара.
     * @return StringProperty с возможностью наблюдения изменений
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Устанавливает новое значение названия товара.
     * @param name новое название
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * Возвращает числовое значение количества товара.
     * @return текущее количество
     */
    public int getQuantity() {
        return quantity.get();
    }

    /**
     * Предоставляет свойство для биндинга количества товара.
     * @return IntegerProperty с возможностью наблюдения изменений
     */
    public IntegerProperty quantityProperty() {
        return quantity;
    }

    /**
     * Устанавливает новое значение количества товара.
     * @param quantity новое количество
     */
    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    /**
     * Возвращает текстовое значение тега товара.
     * @return текущий тег
     */
    public String getTag() {
        return tag.get();
    }

    /**
     * Предоставляет свойство для биндинга тега товара.
     * @return StringProperty с возможностью наблюдения изменений
     */
    public StringProperty tagProperty() {
        return tag;
    }

    /**
     * Устанавливает новое значение тега товара.
     * @param tag новый тег
     */
    public void setTag(String tag) {
        this.tag.set(tag);
    }
}