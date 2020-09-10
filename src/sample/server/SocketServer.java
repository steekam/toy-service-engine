package sample.server;

import sample.models.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class SocketServer {
    private boolean isConnected = false;

    private ServerSocket serverSocket;

    private Consumer<ClientConnection> onNewConnectionCallback;

    private HashMap<String, ClientConnection> connectedClients = new HashMap<>();

    public boolean isConnected() {
        return isConnected;
    }

    public HashMap<String, ClientConnection> getConnectedClientsMap() {
        return connectedClients;
    }

    public void setOnNewConnectionCallback(Consumer<ClientConnection> onNewConnectionCallback) {
        this.onNewConnectionCallback = onNewConnectionCallback;
    }

    public void removeDisconnectedClient(String clientName) {
        connectedClients.remove(clientName);
    }

    public void start(Consumer<Integer> callback) {
        final ExecutorService processingPool = Executors.newFixedThreadPool(10);

        Runnable serverTask = () -> {
            try {
                serverSocket = new ServerSocket(0);
                isConnected = true;
                int connectedPort = serverSocket.getLocalPort();

                callback.accept(connectedPort);

                while (serverSocket.isBound()) {
                    ClientConnection clientConnection = new ClientConnection(serverSocket.accept(), onNewConnectionCallback);

                    // add new connection to pool
                    processingPool.submit(clientConnection);

                    // add to map
                    connectedClients.put(clientConnection.getName(), clientConnection);

                    // send welcome message
                    sendWelcomeMessage(clientConnection);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        (new Thread(serverTask)).start();
    }

    public void disconnect(Runnable callback) {
        new Thread(() -> {
            try {
                serverSocket.close();
                isConnected = false;
                callback.run();
            } catch (IOException e) {
                System.err.println("Unable to disconnect");
                e.printStackTrace();
            }
        }).start();
    }

    private void sendWelcomeMessage(ClientConnection client) throws IOException {
        if (isConnected) {
            client.getObjectOutputStream().writeObject(new Message("Welcome to the Toy Engine Server Client "+client.getClientSocket().getPort()));
        }
    }

    public void sendRequestToClient(String client, String requestType, Runnable callback) {
        connectedClients.get(client).sendRequest(requestType);

        callback.run();
    }
}
