package client.network;

import java.io.*;
import java.net.*;

/**
 * Handles requests to remote (internet) servers via HttpURLConnection.
 */
public class RemoteRequest {

    /**
     * Executes an HTTP or HTTPS request to a remote server.
     *
     * @param method  HTTP method (GET, POST, HEAD)
     * @param urlStr  The full URL, e.g. "https://example.com"
     * @return        Full HTTP response text (headers + body for GET/POST)
     */
    public String execute(String method, String urlStr) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(method.toUpperCase());
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setInstanceFollowRedirects(true);

            // --- For POST ---
            if (method.equalsIgnoreCase("POST")) {
                conn.setDoOutput(true);
                String payload = "client=JavaFX&mode=test";
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(payload.getBytes());
                }
            }

            // --- Status line ---
            response.append("HTTP/1.1 ").append(conn.getResponseCode())
                    .append(" ").append(conn.getResponseMessage()).append("\n");

            // --- Headers ---
            conn.getHeaderFields().forEach((key, values) -> {
                if (key != null) {
                    response.append(key).append(": ")
                            .append(String.join(", ", values)).append("\n");
                }
            });
            response.append("\n");

            // --- Body ---
            if (!method.equalsIgnoreCase("HEAD")) {
                InputStream stream;
                try {
                    stream = conn.getInputStream();
                } catch (IOException e) {
                    // handle error stream (e.g. 404 or 500)
                    stream = conn.getErrorStream();
                }

                if (stream != null) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line).append("\n");
                        }
                    }
                }
            } else {
                // HEAD responses have no body — keep it clean but informative
                response.append("<no body - HEAD request>");
            }

            conn.disconnect();

        } catch (ProtocolException e) {
            return "RemoteRequest Error: Unsupported HTTP method → " + e.getMessage();
        } catch (MalformedURLException e) {
            return "RemoteRequest Error: Invalid URL → " + e.getMessage();
        } catch (IOException e) {
            return "RemoteRequest Error: " + e.getMessage();
        }

        return response.toString();
    }
}
