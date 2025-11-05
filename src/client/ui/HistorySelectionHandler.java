package client.ui;

/**
 * Callback for history selection.
 */
@FunctionalInterface
public interface HistorySelectionHandler {
    void openUrl(String url);
}
