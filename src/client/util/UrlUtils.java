package client.util;

public class UrlUtils {

    /**
     * Determines if a URL is local (localhost or 127.0.0.1).
     *
     * @param url The full or partial URL.
     * @return true if it's local, false if it's remote.
     */
    public static boolean isLocal(String url) {
        if (url == null) return false;
        url = url.trim().toLowerCase();
        return url.startsWith("http://localhost")
                || url.startsWith("http://127.0.0.1")
                || (!url.startsWith("http://") && !url.startsWith("https://"));
    }

    /**
     * Normalizes a user-entered resource path or URL.
     * If the input is like "/index.html" or "about.html", returns "http://localhost:8080/index.html".
     * If the input already looks like a full URL, returns it unchanged.
     *
     * @param input Raw input from the UI text field.
     * @return A valid absolute URL string.
     */
    public static String normalize(String input) {
        if (input == null || input.isEmpty()) {
            return "http://localhost:8080/index.html";
        }

        String trimmed = input.trim();

        // if already a full URL (http:// or https://)
        if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            return trimmed;
        }

        // ensure it starts with a slash
        if (!trimmed.startsWith("/")) {
            trimmed = "/" + trimmed;
        }

        return "http://localhost:8080" + trimmed;
    }

    /**
     * Extracts just the path (e.g. "/index.html") from a full URL.
     *
     * @param url A full or local URL string.
     * @return The path part only (useful for display or local request).
     */
    public static String extractPath(String url) {
        if (url == null || url.isEmpty()) return "/";
        try {
            java.net.URL u = new java.net.URL(normalize(url));
            return u.getPath().isEmpty() ? "/" : u.getPath();
        } catch (Exception e) {
            return "/";
        }
    }

    /**
     * Simple validation — checks if the string looks like a valid URL or local path.
     *
     * @param input The user-entered text.
     * @return true if usable, false if malformed.
     */
    public static boolean isValid(String input) {
        if (input == null || input.trim().isEmpty()) return false;
        try {
            new java.net.URL(normalize(input));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
