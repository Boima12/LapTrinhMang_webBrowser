package client.ui;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.web.WebView;
import javafx.geometry.Insets;
import javafx.scene.Node;
import client.controller.ClientController;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Defines the main UI layout (toolbar, console, WebView) and exposes
 * event handlers for the controller to connect.
 */
public class ClientUI {

    private final VBox root;
    private final ChoiceBox<String> methodChoice;
    private final TextField pathField;
//    private final TextArea consoleArea;
    private final WebView webView;

    // Action callbacks (set by controller)
    private BiConsumer<String, String> onSend;
    private Runnable onHistory;
    private Runnable onBack;
    private Runnable onForward;

    public ClientUI(ClientController controller) {
        methodChoice = new ChoiceBox<>();
        methodChoice.getItems().addAll("GET", "POST", "HEAD");
        methodChoice.setValue("GET");

        pathField = new TextField("http://localhost:8080/index.html");
        pathField.setMinWidth(350);
        HBox.setHgrow(pathField, Priority.ALWAYS);

        Button sendBtn = new Button("Send Request");
        Button historyBtn = new Button("History");
        Button backBtn = new Button("< Back");
        Button forwardBtn = new Button("Forward >");

        // --- Top Bar Layout ---
        HBox topBar = new HBox(10,
            historyBtn, backBtn, forwardBtn,
            new Label("Method:"), methodChoice,
            new Label("URL:"), pathField,
            sendBtn
        );
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-alignment: center-left;");

        // --- Console & WebView ---
//        consoleArea = new TextArea();
//        consoleArea.setPrefRowCount(10);
//        consoleArea.setWrapText(true);

        webView = new WebView();
//        webView.setPrefHeight(500);
        VBox.setVgrow(webView, Priority.ALWAYS);

//        root = new VBox(10, topBar, consoleArea, webView);
        root = new VBox(10, topBar, webView);
        root.setPadding(new Insets(10));

        // --- Button Actions ---
        sendBtn.setOnAction(e -> {
            if (onSend != null) onSend.accept(methodChoice.getValue(), pathField.getText().trim());
        });

        historyBtn.setOnAction(e -> {
            if (onHistory != null) onHistory.run();
        });

        backBtn.setOnAction(e -> {
            if (onBack != null) onBack.run();
        });

        forwardBtn.setOnAction(e -> {
            if (onForward != null) onForward.run();
        });
    }

    /** @return root layout (for Scene) */
    public VBox getRoot() {
        return root;
    }

    // ---- Controller callbacks setters ----
    public void setOnSend(BiConsumer<String, String> handler) { this.onSend = handler; }
    public void setOnHistory(Runnable handler) { this.onHistory = handler; }
    public void setOnBack(Runnable handler) { this.onBack = handler; }
    public void setOnForward(Runnable handler) { this.onForward = handler; }

    // ---- UI update helpers ----
//    public void clearConsole() {
//        consoleArea.clear();
//    }
//
//    public void appendConsole(String text) {
//        consoleArea.appendText(text);
//    }
//
//    public void setConsoleText(String text) {
//        consoleArea.setText(text);
//    }

    public void setPathField(String url) {
        pathField.setText(url);
    }

    /** Load a full URL directly into the WebView (used for GET/POST) */
    public void loadInWebView(String url) {
        webView.getEngine().load(url);
    }

    /** Load raw HTML (used for HEAD responses, etc.) */
    public void loadHtml(String html) {
        webView.getEngine().loadContent(html);
    }
}
