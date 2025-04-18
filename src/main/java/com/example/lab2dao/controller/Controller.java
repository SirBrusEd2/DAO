package com.example.lab2dao.controller;

import com.example.lab2dao.dao.ExcelProductDaoImpl;
import com.example.lab2dao.dao.PostgresProductDaoImpl;
import com.example.lab2dao.dao.ProductDao;
import com.example.lab2dao.dao.ProductDaoImpl;
import com.example.lab2dao.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Контроллер для управления пользовательским интерфейсом приложения.
 * Обеспечивает взаимодействие между UI-компонентами и логикой работы с данными.
 */
public class Controller {
    private ProductDao productDao;
    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> idColumn;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;
    @FXML
    private TableColumn<Product, String> tagColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField tagField;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private ToggleGroup dataSourceToggle;
    @FXML
    private RadioButton memoryRadio;
    @FXML
    private RadioButton excelRadio;
    @FXML
    private RadioButton postgresRadio;

    /**
     * Инициализация UI-компонентов и начальной конфигурации.
     * <p>
     * Настраивает:
     * <ul>
     *     <li>Группу переключателей для выбора источника данных</li>
     *     <li>Привязку колонок таблицы к свойствам объекта Product</li>
     *     <li>Обработчики событий для выбора элементов таблицы и изменения источника данных</li>
     * </ul>
     * Вызывается автоматически после загрузки FXML-файла.
     */
    @FXML
    public void initialize() {
        // Создаем ToggleGroup и связываем с RadioButton
        ToggleGroup dataSourceToggle = new ToggleGroup();
        memoryRadio.setToggleGroup(dataSourceToggle);
        excelRadio.setToggleGroup(dataSourceToggle);
        postgresRadio.setToggleGroup(dataSourceToggle); // Добавьте эту строку
        memoryRadio.setSelected(true); // Выбираем по умолчанию "В памяти"

        // Остальной код инициализации...
        productDao = new ProductDaoImpl();

        // Настройка колонок таблицы
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));

        // Загрузка данных
        refreshTable();

        // Обработчик выбора элемента в таблице
        productTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });

        // Обработчик переключения источника данных
        dataSourceToggle.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == memoryRadio) {
                productDao = new ProductDaoImpl();
            } else if (newToggle == excelRadio) {
                productDao = new ExcelProductDaoImpl();
                productDao.setDataSource("products.xlsx");
            } else if (newToggle == postgresRadio) { // Добавьте этот блок
                productDao = new PostgresProductDaoImpl();
            }
            refreshTable();
        });
    }

    /**
     * Обновляет данные в таблице, загружая актуальную информацию из текущего источника данных.
     * Автоматически вызывается при изменении источника данных или выполнении операций CRUD.
     */
    private void refreshTable() {
        productList.setAll(productDao.getAllProducts());
        productTable.setItems(productList);
    }

    /**
     * Заполняет текстовые поля данными выбранного продукта из таблицы.
     * @param product выбранный продукт для отображения в полях ввода
     */
    private void populateFields(Product product) {
        nameField.setText(product.getName());
        quantityField.setText(String.valueOf(product.getQuantity()));
        tagField.setText(product.getTag());
    }

    /**
     * Очищает текстовые поля ввода.
     * Используется после добавления/обновления/удаления для подготовки к новому вводу.
     */
    private void clearFields() {
        nameField.clear();
        quantityField.clear();
        tagField.clear();
    }

    /**
     * Обрабатывает событие добавления нового продукта.
     * <p>
     * Проверяет корректность ввода числового значения количества.
     * Создает новый продукт с автоматической генерацией ID через DAO.
     * @throws NumberFormatException если поле количества содержит нечисловое значение
     */
    @FXML
    private void handleAdd() {
        try {
            Product product = new Product(
                    0, // ID будет установлен DAO
                    nameField.getText(),
                    Integer.parseInt(quantityField.getText()),
                    tagField.getText()
            );
            productDao.addProduct(product);
            refreshTable();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Ошибка ввода", "Пожалуйста, введите корректное количество (число).");
        }
    }

    /**
     * Обрабатывает событие обновления выбранного продукта.
     * <p>
     * Требует предварительного выбора элемента в таблице.
     * Обновляет все поля продукта, включая ID из исходного объекта.
     * @throws NumberFormatException если поле количества содержит нечисловое значение
     */
    @FXML
    private void handleUpdate() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            try {
                selectedProduct.setName(nameField.getText());
                selectedProduct.setQuantity(Integer.parseInt(quantityField.getText()));
                selectedProduct.setTag(tagField.getText());
                productDao.updateProduct(selectedProduct);
                refreshTable();
                clearFields();
            } catch (NumberFormatException e) {
                showAlert("Ошибка ввода", "Пожалуйста, введите корректное количество (число).");
            }
        } else {
            showAlert("Не выбрано", "Пожалуйста, выберите товар для обновления.");
        }
    }

    /**
     * Обрабатывает событие удаления выбранного продукта.
     * Удаляет продукт по его ID из текущего источника данных.
     */
    @FXML
    private void handleDelete() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            productDao.deleteProduct(selectedProduct.getId());
            refreshTable();
            clearFields();
        } else {
            showAlert("Не выбрано", "Пожалуйста, выберите товар для удаления.");
        }
    }

    /**
     * Отображает диалоговое окно с предупреждением.
     * @param title   заголовок окна
     * @param message текст сообщения для пользователя
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}