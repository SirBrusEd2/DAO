/**
 * Главный класс приложения, запускающий JavaFX-окно.
 *
 * <p>Наследуется от {@link javafx.application.Application} и реализует основной цикл запуска приложения.
 * Отвечает за:
 * <ul>
 *     <li>Инициализацию первичного окна (Stage)</li>
 *     <li>Загрузку FXML-разметки интерфейса</li>
 *     <li>Настройку параметров главного окна приложения</li>
 * </ul>
 *
 * <p>Структура ресурсов:
 * <ul>
 *     <li>FXML-файл интерфейса: "hello-view.fxml"</li>
 *     <li>Контроллер: {@link Controller}</li>
 * </ul>
 *
 * @see javafx.application.Application
 */
package com.example.lab2dao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    /**
     * Точка входа для инициализации JavaFX-приложения.
     *
     * @param stage первичное окно приложения (автоматически создается JavaFX)
     * @throws IOException если произошла ошибка загрузки FXML-файла
     *
     * <p>Логика работы метода:
     * <ol>
     *     <li>Загрузка FXML-разметки через FXMLLoader</li>
     *     <li>Создание сцены с указанными размерами (1000x700 пикселей)</li>
     *     <li>Настройка заголовка окна</li>
     *     <li>Отображение окна</li>
     * </ol>
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Управление товарами");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Основной метод запуска приложения.
     *
     * @param args аргументы командной строки (не используются)
     *
     * <p>Особенности реализации:
     * <ul>
     *     <li>Делегирует запуск JavaFX-платформе через {@link Application#launch()}</li>
     *     <li>Является точкой входа при запуске из IDE или исполняемого JAR-файла</li>
     * </ul>
     */

    public static void main(String[] args) {
        launch();
    }
}