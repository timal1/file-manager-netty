package com.timal.UI;

import com.timal.manager.FileWorker;
import com.timal.manager.Network;
import com.timal.messages.*;
import javafx.stage.FileChooser;
import lombok.Data;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;

@Data
@Log4j2
public class Controller implements Initializable {

    public final static long CAPACITY_CLOUD_IN_GB = 10;

    private static Network network;

    private PanelControllerServer serverPC;
    private PanelController clientPC;
    private FileChooser fileChooser;
    private FileWorker fileWorker;
    private String userName;

    public PanelControllerServer getServerPC() {
        return serverPC;
    }

    public static Network getNetwork() {
        return network;
    }

    @FXML
    VBox cloudPane;

    @FXML
    TableView<FileInfoMessage> tableView;

    @FXML
    GridPane regPane;
    @FXML
    TextField regLogin;
    @FXML
    PasswordField regPassword;
    @FXML
    PasswordField regPasswordRep;
    @FXML
    TextField regName;
    @FXML
    public
    Label regMessage;

    @FXML
    TextField authLogin;
    @FXML
    PasswordField authPassword;
    @FXML
    GridPane authPane;
    @FXML
    public
    Label authMessage;

    @FXML
    ProgressBar progressBar;
    @FXML
    Label fileNameMessage;

    @FXML
    VBox load_bar;
    @FXML
    VBox bar;

    @FXML
    Label fileSizeLabel;

    @FXML
    VBox leftPanel, rightPanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changeStageToAuth();
        serverPC = (PanelControllerServer) rightPanel.getProperties().get("ctrl");
        clientPC = (PanelController) leftPanel.getProperties().get("ctrl");
        this.fileWorker = new FileWorker();
    }

    @FXML
    public void changeStageToAuth() {
        Platform.runLater(() -> {
            authLogin.clear();
            authPassword.clear();
        });
        authPane.setVisible(true);
        authMessage.setVisible(false);
        regPane.setVisible(false);
        cloudPane.setVisible(false);
    }

    @FXML
    public void changeStageToReg() {
        Platform.runLater(() -> {
            regLogin.clear();
            regPassword.clear();
            regPasswordRep.clear();
            regName.clear();
        });
        regPane.setVisible(true);
        regMessage.setVisible(false);
        authPane.setVisible(false);
        cloudPane.setVisible(false);
    }

    @FXML
    public void enterCloud() throws InterruptedException {
        if (network == null) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            network = new Network(this, countDownLatch);
            getServerPC().setNetwork(network);
            new Thread(network).start();
            countDownLatch.await();
        }
        if (authLogin.getText().isEmpty() || authPassword.getText().isEmpty()) {
            authMessage.setText("Enter login and password");
            authMessage.setVisible(true);
        } else {
            String login = authLogin.getText();
            String pass = authPassword.getText();

            network.send(new AuthMessage(login, pass));
        }
    }

    @FXML
    public void register() throws InterruptedException {
        if (network == null) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            network = new Network(this, countDownLatch);
            new Thread(network).start();
            countDownLatch.await();
        }
        if (regLogin.getText().isEmpty() || regPassword.getText().isEmpty() ||
                regPasswordRep.getText().isEmpty() || regName.getText().isEmpty()) {
            regMessage.setTextFill(Color.RED);
            regMessage.setText("Enter login, password and name");
            regMessage.setVisible(true);
        } else if (!regPassword.getText().equals(regPasswordRep.getText())) {
            regMessage.setTextFill(Color.RED);
            regMessage.setText("Passwords do not match");
            regMessage.setVisible(true);
        } else {
            userName = regLogin.getText();
            RegisterMessage registerMessage = new RegisterMessage(regName.getText(), regLogin.getText(), regPassword.getText());
            network.send(registerMessage);
        }
    }


    @FXML
    public void moveBtnAction() {

        if (clientPC.getSelectedFileName() == null && serverPC.getSelectedFileNameServer() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "File not is selected", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // если выбран левый фаил, то перемещаем с клиента на сервер
        if (clientPC.getSelectedFileName() != null) {

            String clientPCPath = Paths.get(clientPC.getCurrentPath(), clientPC.getSelectedFileName()).toString();
            String nameServerPCPath = serverPC.getCurrentPath();
            String nameClientPcPath = clientPC.getCurrentPath();

            File file = new File(clientPCPath);
            if (file != null) {
                long sizeStream = serverPC.getFileList()
                        .stream()
                        .map((p) -> p.getFilename())
                        .filter((p) -> p.equals(file.getName()))
                        .count();
                fileWorker.working(file, this::setVisibleLoadInfoFile, sizeStream > 0,
                        nameServerPCPath, nameClientPcPath, true);
            }
        }

        // если выбран правый фаил, то перемещаем с сервера на клиент
        if (serverPC.getSelectedFileNameServer() != null) {

            String fileName = serverPC.getSelectedFileNameServer();
            String nameServerPCPath = serverPC.getCurrentPath();
            String nameClientPcPath = clientPC.getCurrentPath();
            FileRequestMessage fileRequestMessage = new FileRequestMessage(fileName, nameServerPCPath,
                    nameClientPcPath, true);
            network.send(fileRequestMessage);
        }
    }

    @FXML
    public void btnExitAction() {
        network.close();
        Platform.exit();
    }

    @FXML
    public void copyBtnAction() {

        if (clientPC.getSelectedFileName() == null && serverPC.getSelectedFileNameServer() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "File not is selected", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // если выбран левый фаил, то копируем с клиента на сервер
        if (clientPC.getSelectedFileName() != null) {
            String clientPCPath = Paths.get(clientPC.getCurrentPath(),
                    clientPC.getSelectedFileName()).toString();
            String nameServerPCPath = serverPC.getCurrentPath();
            String nameClientPcPath = clientPC.getCurrentPath();
            File file = new File(clientPCPath);
            if (file != null) {
                long sizeStream = serverPC.getFileList()
                        .stream()
                        .map((p) -> p.getFilename())
                        .filter((p) -> p.equals(file.getName()))
                        .count();
                fileWorker.working(file, this::setVisibleLoadInfoFile, sizeStream > 0,
                        nameServerPCPath, nameClientPcPath, false);
            }
        }

        // если выбран правый фаил, то копируем с сервера на клиент
        if (serverPC.getSelectedFileNameServer() != null) {
            String nameServerPCPath = Paths.get(serverPC.getCurrentPath()).toString();
            String nameClientPcPath = clientPC.getCurrentPath();
            String fileName = serverPC.getSelectedFileNameServer();
            FileRequestMessage fileRequestMessage = new FileRequestMessage(fileName, nameServerPCPath, nameClientPcPath,
                    false);
            network.send(fileRequestMessage);
        }
    }

    @FXML
    public void deleteBtnAction() {
        if (clientPC.getSelectedFileName() == null && serverPC.getSelectedFileNameServer() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "File not is selected", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (clientPC.getSelectedFileName() != null) {
            String deleteFileName = clientPC.getSelectedFileName();
            String clientPCPath = Paths.get(clientPC.getCurrentPath()).toString();
            deleteFile(clientPCPath, deleteFileName);
        }

        if (serverPC.getSelectedFileNameServer() != null) {
            String namePathServerPC = Paths.get(serverPC.getCurrentPath()).toString();
            String deleteFileName = serverPC.getSelectedFileNameServer();
            DelFileRequestMessage delFileRequestMessage = new DelFileRequestMessage(deleteFileName, namePathServerPC);
            network.send(delFileRequestMessage);
        }
    }

    @SneakyThrows
    public void deleteFile(String clientPCPath, String nameDelFile) {
        Path path = Paths.get(clientPCPath + "\\" + nameDelFile);
        Files.delete(path);
        log.info("Пользователь " + userName + " удалил файл " + nameDelFile);
        clientPC.updateList(Paths.get(clientPCPath));
    }

    public void changeProgressBar(double percent) {
        progressBar.setProgress(percent);
    }

    public void changeLoadBar(double sizeFiles) {
        double onePercentLoadBar = load_bar.getHeight() / 100;
        double PercentAllFiles = 100 / (CAPACITY_CLOUD_IN_GB / sizeFiles);
        bar.setPrefHeight(PercentAllFiles * onePercentLoadBar);
    }

    public void setVisibleLoadInfoFile(boolean check) {
        fileNameMessage.setText("Загрузка файла.");
        fileNameMessage.setVisible(check);
        progressBar.setVisible(check);
    }

    public void changeStageToCloud() {
        cloudPane.setVisible(true);
        authPane.setVisible(false);
        regPane.setVisible(false);
    }
}
