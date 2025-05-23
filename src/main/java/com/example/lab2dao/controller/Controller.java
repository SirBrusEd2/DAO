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
import javafx.util.Duration;

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
     *     <li>Подсказки для кнопок с задержкой отображения</li>
     * </ul>
     * Вызывается автоматически после загрузки FXML-файла.
     */
    @FXML
    public void initialize() {
        // Создаем ToggleGroup и связываем с RadioButton
        ToggleGroup dataSourceToggle = new ToggleGroup();
        memoryRadio.setToggleGroup(dataSourceToggle);
        excelRadio.setToggleGroup(dataSourceToggle);
        postgresRadio.setToggleGroup(dataSourceToggle);
        memoryRadio.setSelected(true);

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
            } else if (newToggle == postgresRadio) {
                productDao = new PostgresProductDaoImpl();
            }
            refreshTable();
        });

        // Настройка подсказок для кнопок с задержкой 3 секунды
        setupButtonTooltip(addButton, "Добавить новый товар в текущий источник данных");
        setupButtonTooltip(updateButton, "Обновить выбранный товар");
        setupButtonTooltip(deleteButton, "Удалить выбранный товар");
    }

    /**
     * Настраивает Tooltip для кнопки с указанной задержкой отображения.
     *
     * @param button кнопка, для которой настраивается подсказка
     * @param text текст подсказки, который будет отображаться пользователю
     *
     * <p>Особенности реализации:
     * <ul>
     *     <li>Создает новый экземпляр Tooltip с заданным текстом</li>
     *     <li>Устанавливает задержку отображения в 3 секунды</li>
     *     <li>Привязывает подсказку к указанной кнопке</li>
     * </ul>
     */
    private void setupButtonTooltip(Button button, String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setShowDelay(Duration.seconds(3));
        button.setTooltip(tooltip);
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
     *
     * @param product выбранный продукт для отображения в полях ввода
     *
     * <p>Заполняет следующие поля:
     * <ul>
     *     <li>Поле названия (nameField)</li>
     *     <li>Поле количества (quantityField)</li>
     *     <li>Поле метки (tagField)</li>
     * </ul>
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
     *
     * @throws NumberFormatException если поле количества содержит нечисловое значение
     *
     * <p>Логика работы:
     * <ol>
     *     <li>Проверяет валидность ввода количества</li>
     *     <li>Создает новый объект Product</li>
     *     <li>Добавляет продукт через DAO</li>
     *     <li>Обновляет таблицу</li>
     *     <li>Очищает поля ввода</li>
     * </ol>
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
     *
     * @throws NumberFormatException если поле количества содержит нечисловое значение
     *
     * <p>Логика работы:
     * <ol>
     *     <li>Проверяет наличие выбранного продукта</li>
     *     <li>Проверяет валидность ввода количества</li>
     *     <li>Обновляет данные продукта</li>
     *     <li>Сохраняет изменения через DAO</li>
     *     <li>Обновляет таблицу</li>
     *     <li>Очищает поля ввода</li>
     * </ol>
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
     *
     * <p>Логика работы:
     * <ol>
     *     <li>Проверяет наличие выбранного продукта</li>
     *     <li>Удаляет продукт через DAO</li>
     *     <li>Обновляет таблицу</li>
     *     <li>Очищает поля ввода</li>
     * </ol>
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
     *
     * @param title заголовок окна
     * @param message текст сообщения для пользователя
     *
     * <p>Особенности реализации:
     * <ul>
     *     <li>Использует Alert типа WARNING</li>
     *     <li>Не содержит заголовка (headerText = null)</li>
     *     <li>Блокирует взаимодействие с другими окнами до закрытия</li>
     * </ul>
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}