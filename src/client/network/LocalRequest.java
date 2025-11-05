package client.network;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Handles HTTP requests to any local network host.
 */
public class LocalRequest {

    /**
     * Executes a local HTTP request and returns headers + body.
     *
     * @param method   HTTP method (GET, POST, HEAD)
     * @param resource full URL
     * @return String[] → [0] headers, [1] body (empty for HEAD)
     */
    public String[] execute(String method, String resource) {
        StringBuilder headers = new StringBuilder();
        StringBuilder body = new StringBuilder();

        try {
            URI uri = new URI(resource);
            String host = uri.getHost() != null ? uri.getHost() : "localhost";
            int port = (uri.getPort() != -1) ? uri.getPort() : 8080;
            String path = uri.getPath() != null && !uri.getPath().isEmpty() ? uri.getPath() : "/";

            try (Socket socket = new Socket(host, port);
                 PrintWriter out = new PrintWriter(socket.getOutputStream());
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // --- Build request ---
                out.print(method + " " + path + " HTTP/1.1\r\n");
                out.print("Host: " + host + "\r\n");
                out.print("Connection: close\r\n");

                if (method.equalsIgnoreCase("POST")) {
                    String payload = "name=Boima&language=JavaFX";
                    out.print("Content-Type: application/x-www-form-urlencoded\r\n");
                    out.print("Content-Length: " + payload.length() + "\r\n");
                    out.print("\r\n");
                    out.print(payload);
                } else {
                    out.print("\r\n");
                }

                out.flush();

                // --- Read headers ---
                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    headers.append(line).append("\n");
                }

                // --- Read body only for GET/POST ---
                if (!method.equalsIgnoreCase("HEAD")) {
                    while ((line = in.readLine()) != null) {
                        body.append(line).append("\n");
                    }
                }
            }

        } catch (URISyntaxException e) {
            return new String[]{"LocalRequest Error (invalid URL): " + e.getMessage(), ""};
        } catch (IOException e) {
            return new String[]{"LocalRequest Error (network): " + e.getMessage(), ""};
        }

        return new String[]{headers.toString(), body.toString()};
    }
}
