package com.timal.UI;

import com.timal.manager.Network;
import com.timal.messages.FileListMessage;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import com.timal.messages.FileInfoMessage;
import lombok.Data;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Data
public class PanelControllerServer implements Initializable {

    private Network network;
    List<FileInfoMessage> fileList = new ArrayList<>();
    private String pathNameGetFromServer;


    @FXML
    TableView<FileInfoMessage> filesTable;

    @FXML
    TextField pathField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        TableColumn<FileInfoMessage, String> fileTypeColumn = new TableColumn<>();
        fileTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType().getName()));
        fileTypeColumn.setPrefWidth(24);

        TableColumn<FileInfoMessage, String> fileNameColumn = new TableColumn<>("Name");
        fileNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFileName()));
        fileNameColumn.setPrefWidth(240);

        TableColumn<FileInfoMessage, Long> fileSizeColumn = new TableColumn<>("Size");
        fileSizeColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSize()));
        fileSizeColumn.setCellFactory(column -> {
            return new TableCell<FileInfoMessage, Long>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        String text = String.format("%,d bytes", item);
                        if (item == -1L) {
                            text = "[DIR]";
                        }
                        setText(text);
                    }
                }
            };
        });
        fileSizeColumn.setPrefWidth(120);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        TableColumn<FileInfoMessage, String> fileDateColumn = new TableColumn<>("Date changed");
        fileDateColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastModified().format(dtf)));
        fileDateColumn.setPrefWidth(120);

        filesTable.getColumns().addAll(fileTypeColumn, fileNameColumn, fileSizeColumn, fileDateColumn);
        filesTable.getSortOrder().add(fileTypeColumn);

        filesTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    FileInfoMessage.FileType fileType = filesTable.getSelectionModel().getSelectedItem().getType();
                    String filePath = filesTable.getSelectionModel().getSelectedItem().getPath();
                    if (fileType.equals(FileInfoMessage.FileType.DIRECTORY)) {
                        FileListMessage fileListMessage = new FileListMessage(filePath);
                        network.send(fileListMessage);
                    }
                }
            }
        });
    }

    @FXML
    public void btnPathUpAction() {
        Path upperPath = Paths.get(pathField.getText()).getParent();
        if (upperPath != null) {

            FileListMessage fileListMessage = new FileListMessage(upperPath.toString());
            network.send(fileListMessage);
        }
    }

    public String getSelectedFileNameServer() {
        if (!filesTable.isFocused()) {
            return null;
        }
        return filesTable.getSelectionModel().getSelectedItem().getFileName();
    }

    public void updateList(List<FileInfoMessage> fileInfoMessages, String pathName) {
        fileList = fileInfoMessages;
        filesTable.getItems().clear();
        pathNameGetFromServer = pathName;
        pathField.setText(pathName);
        if (fileInfoMessages != null) {
            filesTable.getItems().addAll(fileInfoMessages);
        }
        filesTable.sort();
    }

    public String getCurrentPath() {
        return pathField.getText();
    }

}
