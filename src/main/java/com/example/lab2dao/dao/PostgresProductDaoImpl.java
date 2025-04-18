/**
 * Реализация интерфейса ProductDao для работы с PostgreSQL базой данных.
 * Использует JDBC для выполнения CRUD-операций с товарами в СУБД.
 *
 * <p>Особенности реализации:
 * <ul>
 *     <li>Автоматическое создание таблицы при инициализации</li>
 *     <li>Использование пула соединений (одно соединение на экземпляр)</li>
 *     <li>Поддержка транзакций на уровне отдельных операций</li>
 *     <li>Жестко заданные параметры подключения в конструкторе</li>
 * </ul>
 *
 * @see ProductDao
 */
package com.example.lab2dao.dao;

import com.example.lab2dao.model.Product;
import javafx.scene.control.Alert;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostgresProductDaoImpl implements ProductDao {
    private Connection connection;

    /**
     * Создает соединение с PostgreSQL и инициализирует схему БД.
     * @throws RuntimeException если не удалось установить соединение
     *
     * <p>Параметры подключения по умолчанию:
     * <ul>
     *     <li>URL: jdbc:postgresql://localhost:7777/dao</li>
     *     <li>Пользователь: postgres</li>
     *     <li>Пароль: postgres</li>
     * </ul>
     */
    public PostgresProductDaoImpl() {
        try {
            String url = "jdbc:postgresql://localhost:7777/dao";
            String user = "postgres";
            String password = "postgres";
            connection = DriverManager.getConnection(url, user, password);
            createTableIfNotExists();
        } catch (SQLException e) {
            showConnectionErrorAlert(e);
            throw new RuntimeException("Failed to connect to PostgreSQL", e);
        }
    }

    /**
     * Создает таблицу products, если она не существует.
     * @throws SQLException при ошибках выполнения DDL-запроса
     */
    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS products (" +
                "id SERIAL PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "tag VARCHAR(255)" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    /**
     * Добавляет новый товар в базу данных.
     * @param product добавляемый товар (ID генерируется базой данных)
     */
    @Override
    public void addProduct(Product product) {
        String sql = "INSERT INTO products (name, quantity, tag) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setProductParameters(pstmt, product);
            pstmt.executeUpdate();
            updateProductIdFromGeneratedKeys(pstmt, product);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    /**
     * Обновляет данные существующего товара в базе.
     * @param product товар с обновленными данными (ищется по ID)
     */
    @Override
    public void updateProduct(Product product) {
        String sql = "UPDATE products SET name = ?, quantity = ?, tag = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            setProductParameters(pstmt, product);
            pstmt.setInt(4, product.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    /**
     * Удаляет товар по указанному ID из базы данных.
     * @param id идентификатор удаляемого товара
     */
    @Override
    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    /**
     * Получает список всех товаров из базы данных.
     * @return список товаров. Возвращает пустой список при ошибках выполнения запроса.
     */
    @Override
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return products;
    }

    @Override
    public void setDataSource(String source) {
        // Реализация переключения БД может быть добавлена здесь
    }

    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getInt("quantity"),
                rs.getString("tag")
        );
    }

    private void setProductParameters(PreparedStatement pstmt, Product product) throws SQLException {
        pstmt.setString(1, product.getName());
        pstmt.setInt(2, product.getQuantity());
        pstmt.setString(3, product.getTag());
    }

    private void updateProductIdFromGeneratedKeys(PreparedStatement pstmt, Product product) throws SQLException {
        try (ResultSet rs = pstmt.getGeneratedKeys()) {
            if (rs.next()) {
                product.setId(rs.getInt(1));
            }
        }
    }

    private void showConnectionErrorAlert(SQLException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка подключения");
        alert.setHeaderText("Не удалось подключиться к PostgreSQL");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private void handleSQLException(SQLException e) {
        e.printStackTrace();
        // Здесь может быть добавлена дополнительная обработка ошибок
    }
}