package client.ui;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

/**
 * Simple history popup panel.
 */
public class HistoryPanel {

    private final Stage stage;
    private final ListView<String> listView;
    private final HistorySelectionHandler handler;

    public HistoryPanel(HistorySelectionHandler handler, List<String> history, int currentIndex) {
        this.handler = handler;
        listView = new ListView<>(FXCollections.observableArrayList(history));
        stage = buildStage(history, currentIndex);
    }

    private Stage buildStage(List<String> history, int currentIndex) {
        Stage s = new Stage();
        s.initModality(Modality.APPLICATION_MODAL);
        s.setTitle("History (" + history.size() + ")");
        s.setMinWidth(600);
        s.setMinHeight(400);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        if (!history.isEmpty()) {
            listView.getSelectionModel().select(Math.max(0, currentIndex));
        }
        listView.setPrefHeight(300);

        // double-click to open
        listView.setOnMouseClicked(ev -> {
            if (ev.getButton() == MouseButton.PRIMARY && ev.getClickCount() == 2) {
                String sel = listView.getSelectionModel().getSelectedItem();
                if (sel != null) performOpen(sel);
            }
        });

        Button openBtn = new Button("Open");
        Button deleteBtn = new Button("Delete");
        Button closeBtn = new Button("Close");
        Button copyBtn = new Button("Copy URL");

        openBtn.setOnAction(ev -> {
            String sel = listView.getSelectionModel().getSelectedItem();
            if (sel != null) performOpen(sel);
        });

        deleteBtn.setOnAction(ev -> {
            int idx = listView.getSelectionModel().getSelectedIndex();
            if (idx >= 0) listView.getItems().remove(idx);
        });

        copyBtn.setOnAction(ev -> {
            String sel = listView.getSelectionModel().getSelectedItem();
            if (sel != null) {
                javafx.scene.input.Clipboard cb = javafx.scene.input.Clipboard.getSystemClipboard();
                javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
                content.putString(sel);
                cb.setContent(content);
            }
        });

        closeBtn.setOnAction(ev -> s.close());

        HBox buttons = new HBox(8, openBtn, deleteBtn, copyBtn, closeBtn);
        buttons.setPadding(new Insets(8));

        TextField filterField = new TextField();
        filterField.setPromptText("Filter history...");
        filterField.textProperty().addListener((obs, oldV, newV) -> {
            if (newV == null || newV.isBlank()) {
                listView.setItems(FXCollections.observableArrayList(history));
            } else {
                String lower = newV.toLowerCase();
                listView.setItems(FXCollections.observableArrayList(
                        history.stream().filter(u -> u.toLowerCase().contains(lower)).toList()
                ));
            }
        });

        BorderPane topBox = new BorderPane();
        topBox.setLeft(filterField);
        topBox.setRight(new Label("Double-click to open"));
        topBox.setPadding(new Insets(0, 0, 8, 0));

        root.setTop(topBox);
        root.setCenter(listView);
        root.setBottom(buttons);

        s.setScene(new Scene(root));
        return s;
    }

    private void performOpen(String url) {
        if (handler != null) handler.openUrl(url);
        stage.close();
    }

    public void show() {
        stage.show();
    }

    public void showAndWait() {
        stage.showAndWait();
    }
}
