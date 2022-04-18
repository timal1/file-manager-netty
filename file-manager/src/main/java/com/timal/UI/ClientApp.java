package com.timal.UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;


public class ClientApp extends Application {

    private FXMLLoader loader;
    private Controller controller;
    private static Stage pStage;

    @Override
    @SneakyThrows
    public void start(Stage primaryStage) {
        this.pStage = primaryStage;
        loader = new FXMLLoader(getClass().getResource("/panelMain.fxml"));
        controller = loader.getController();
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("FileManager");
        primaryStage.show();
    }

    @SneakyThrows
    public void stop() {
        controller = loader.getController();
        controller.getNetwork().close();
    }

    public static void main(String[] args) {
        launch();
    }
}
