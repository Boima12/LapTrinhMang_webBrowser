package client.controller;

import client.network.RequestHandler;
import client.network.RequestHandler.RequestCallback;
import client.ui.ClientUI;
import client.ui.HistoryPanel;
import client.ui.HistorySelectionHandler;
import client.util.UrlUtils;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

/**
 * Coordinates the UI, request handling, and history logic.
 */
public class ClientController {

    private final ClientUI ui;
    private final RequestHandler requestHandler;
    private final HistoryController historyController;

    public ClientController() {
        this.requestHandler = new RequestHandler();
        this.historyController = new HistoryController();
        this.ui = new ClientUI(this);
        initHandlers();
    }

    public VBox getRoot() {
        return ui.getRoot();
    }

    private void initHandlers() {
        ui.setOnSend((method, url) -> {
            String normalized = UrlUtils.normalize(url);
            ui.setPathField(normalized);
            historyController.addToHistory(normalized);
            sendRequest(method, normalized);
        });

        ui.setOnBack(() -> {
            String prev = historyController.goBack();
            if (prev != null) {
                ui.setPathField(prev);
                sendRequest("GET", prev);
            }
        });

        ui.setOnForward(() -> {
            String next = historyController.goForward();
            if (next != null) {
                ui.setPathField(next);
                sendRequest("GET", next);
            }
        });

        ui.setOnHistory(() -> {
            HistorySelectionHandler handler = url -> {
                ui.setPathField(url);
                sendRequest("GET", url);
                historyController.setCurrentIndexTo(url);
            };
            new HistoryPanel(handler, historyController.getHistory(), historyController.getCurrentIndex()).show();
        });
    }

    /** Executes the HTTP request asynchronously. */
    public void sendRequest(String method, String url) {
        requestHandler.sendRequest(method, url, new RequestCallback() {
            @Override
            public void onComplete(String headers, String body, boolean isError) {
                Platform.runLater(() -> {
                    if (isError) {
                        ui.loadHtml("<pre style='color:red;'>" + body + "</pre>");
                        return;
                    }

                    if (method.equalsIgnoreCase("GET") || method.equalsIgnoreCase("POST")) {
                        ui.loadInWebView(url);
                    } else if (method.equalsIgnoreCase("HEAD")) {
                        // Show headers neatly formatted
                        String formattedHeaders = formatHeadersAsHtml(headers);
                        ui.loadHtml(formattedHeaders);
                    }
                });
            }
        });
    }

    /** Converts raw HTTP headers to readable HTML */
    private String formatHeadersAsHtml(String headers) {
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family:monospace;'>")
            .append("<h3>HEAD Response Headers</h3><hr><pre>")
            .append(headers.replaceAll("<", "&lt;").replaceAll(">", "&gt;"))
            .append("</pre></body></html>");
        return html.toString();
    }
}
