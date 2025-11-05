package client.network;

import javafx.concurrent.Task;

import java.net.InetAddress;
import java.net.URI;

/**
 * Central request manager.
 * Chooses between local and remote request handling automatically.
 */
public class RequestHandler {

    private final LocalRequest localRequest = new LocalRequest();
    private final RemoteRequest remoteRequest = new RemoteRequest();

    public void sendRequest(String method, String url, RequestCallback callback) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    String headers = "";
                    String body = "";

                    if (isLocalNetwork(url)) {
                        String[] result = localRequest.execute(method, url);
                        headers = result[0];
                        body = result[1];
                    } else {
                        body = remoteRequest.execute(method, url);
                    }

                    callback.onComplete(headers, body, false);

                } catch (Exception e) {
                    callback.onComplete("", e.getMessage(), true);
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    private boolean isLocalNetwork(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            if (host == null) return false;

            if (host.equalsIgnoreCase("localhost") || host.equals("127.0.0.1")) return true;

            InetAddress addr = InetAddress.getByName(host);
            byte[] bytes = addr.getAddress();

            int b1 = bytes[0] & 0xFF;
            int b2 = bytes[1] & 0xFF;

            if (b1 == 10) return true;
            if (b1 == 172 && (b2 >= 16 && b2 <= 31)) return true;
            if (b1 == 192 && b2 == 168) return true;

            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public interface RequestCallback {
        void onComplete(String headers, String body, boolean isError);
    }
}
