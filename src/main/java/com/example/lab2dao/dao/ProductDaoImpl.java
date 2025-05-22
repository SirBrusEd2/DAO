/**
 * In-memory реализация интерфейса ProductDao для хранения данных в оперативной памяти.
 *
 * <p>Особенности реализации:
 * <ul>
 *     <li>Данные хранятся в ArrayList</li>
 *     <li>Автоматическая генерация ID через инкрементный счетчик</li>
 *     <li>Не сохраняет данные между сеансами работы приложения</li>
 *     <li>Не потокобезопасная реализация</li>
 * </ul>
 *
 * <p>Рекомендуется использовать для:
 * <ul>
 *     <li>Тестирования компонентов</li>
 *     <li>Прототипирования функционала</li>
 *     <li>Демонстрационных целей</li>
 * </ul>
 *
 * @see ProductDao
 */
package com.example.lab2dao.dao;

import com.example.lab2dao.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    private final List<Product> products = new ArrayList<>();
    private int nextId = 1;

    /**
     * Добавляет новый продукт с автоматически сгенерированным ID.
     * @param product добавляемый продукт (существующий ID будет перезаписан)
     */
    @Override
    public void addProduct(Product product) {
        product.setId(nextId++);
        products.add(product);
    }

    /**
     * Обновляет существующий продукт по ID.
     * @param product продукт с обновленными данными (должен содержать существующий ID)
     */
    @Override
    public void updateProduct(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == product.getId()) {
                products.set(i, product);
                break;
            }
        }
    }

    /**
     * Удаляет продукт по указанному ID.
     * @param id идентификатор удаляемого продукта
     */
    @Override
    public void deleteProduct(int id) {
        products.removeIf(product -> product.getId() == id);
    }

    /**
     * Возвращает копию списка всех продуктов.
     * @return новый ArrayList с текущими данными
     */
    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Заглушка для совместимости с интерфейсом (не выполняет действий).
     * @param source игнорируется в данной реализации
     */
    @Override
    public void setDataSource(String source) {
        // In-memory реализация не требует внешних источников данных
    }
}