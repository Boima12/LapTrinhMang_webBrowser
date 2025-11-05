package client.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages navigation history (back, forward, and current index).
 */
public class HistoryController {

    private final List<String> history = new ArrayList<>();
    private int currentIndex = -1;

    public HistoryController() {
    }

    /** Add a new URL to the history */
    public void addToHistory(String url) {
        if (currentIndex < history.size() - 1) {
            history.subList(currentIndex + 1, history.size()).clear();
        }
        history.add(url);
        currentIndex = history.size() - 1;
    }

    /** Get all stored URLs */
    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    /** Get current index */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /** Go back in history */
    public String goBack() {
        if (currentIndex > 0) return history.get(--currentIndex);
        return null;
    }

    /** Go forward in history */
    public String goForward() {
        if (currentIndex < history.size() - 1) return history.get(++currentIndex);
        return null;
    }

    /** Set current index based on URL */
    public void setCurrentIndexTo(String url) {
        int idx = history.indexOf(url);
        if (idx >= 0) currentIndex = idx;
    }
}
