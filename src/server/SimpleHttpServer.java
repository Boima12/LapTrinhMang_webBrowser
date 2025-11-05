package server;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class SimpleHttpServer {
    private final int port = 8080;
    private final File rootDir = new File("www");

    public static void main(String[] args) {
        new SimpleHttpServer().start();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running on port " + port + "...");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleRequest(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             OutputStream out = socket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null) return;

            System.out.println("Request: " + requestLine);
            String[] parts = requestLine.split(" ");
            if (parts.length < 2) return;

            String method = parts[0];
            String resource = parts[1];

            if (resource.equals("/")) {
                resource = "/index.html"; // default root
            }

            File file = new File(rootDir, resource);

            // --- React build fallback ---
            // if file not found but path looks like a React route (no extension), serve /art/index.html
            if (!file.exists() && !resource.contains(".")) {
                file = new File(rootDir, "art/index.html");
            }

            if (!file.exists()) {
                send404(out);
                return;
            }

            if (method.equals("HEAD")) {
                sendHeaders(out, 200, file);
            } else if (method.equals("GET")) {
                sendHeaders(out, 200, file);
                Files.copy(file.toPath(), out);
            } else if (method.equals("POST")) {
                // Basic POST echo handler (can improve later)
                StringBuilder body = new StringBuilder();
                while (in.ready()) {
                    body.append((char) in.read());
                }
                String response = "<html><body><h2>POST Received!</h2><p>Body:</p><pre>" +
                        body.toString() + "</pre></body></html>";
                sendTextResponse(out, 200, response);
            } else {
                sendTextResponse(out, 405, "Method Not Allowed");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendHeaders(OutputStream out, int code, File file) throws IOException {
        PrintWriter pw = new PrintWriter(out);
        pw.println("HTTP/1.1 " + code + " OK");
        pw.println("Content-Type: " + getMimeType(file.getName()));
        pw.println("Content-Length: " + file.length());
        pw.println("Connection: close");
        pw.println();
        pw.flush();
    }

    private void send404(OutputStream out) throws IOException {
        sendTextResponse(out, 404, "<html><body><h1>404 Not Found</h1></body></html>");
    }

    private void sendTextResponse(OutputStream out, int code, String html) throws IOException {
        PrintWriter pw = new PrintWriter(out);
        pw.println("HTTP/1.1 " + code + " OK");
        pw.println("Content-Type: text/html; charset=UTF-8");
        pw.println("Content-Length: " + html.getBytes().length);
        pw.println("Connection: close");
        pw.println();
        pw.flush();
        out.write(html.getBytes());
        out.flush();
    }

    // --- MIME type detection ---
    private String getMimeType(String filename) {
        String name = filename.toLowerCase();
        if (name.endsWith(".html") || name.endsWith(".htm")) return "text/html";
        if (name.endsWith(".css")) return "text/css";
        if (name.endsWith(".js")) return "application/javascript";
        if (name.endsWith(".json")) return "application/json";
        if (name.endsWith(".png")) return "image/png";
        if (name.endsWith(".jpg") || name.endsWith(".jpeg")) return "image/jpeg";
        if (name.endsWith(".ico")) return "image/x-icon";
        if (name.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }
}
