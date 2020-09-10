package sample.client;

import sample.models.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

public class SocketClient {
    private boolean isConnectedToServer = false;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private Socket serverSocket;

    private Consumer<String> onServerInputHandler;

    private Consumer<String> onErrorHandler;

    public void setOnServerDisconnectHandler(Runnable onServerDisconnectHandler) {
        this.onServerDisconnectHandler = onServerDisconnectHandler;
    }

    private Runnable onServerDisconnectHandler;

    public boolean isConnectedToServer() {
        return isConnectedToServer;
    }

    public void setOnServerInputHandler(Consumer<String> consumer) {
        onServerInputHandler = consumer;
    }

    public void setOnErrorHandler(Consumer<String> consumer) {
        onErrorHandler = consumer;
    }

    public void connectToServer(String serverHostname, int serverPort, Runnable onServerConnectedCallback) {
        new Thread(() -> {
            try {
                serverSocket = new Socket(serverHostname, serverPort);

                isConnectedToServer = true;

                onServerConnectedCallback.run();

                objectInputStream = new ObjectInputStream(serverSocket.getInputStream());
                objectOutputStream = new ObjectOutputStream(serverSocket.getOutputStream());

                // handle input from server
                while (serverSocket.getInputStream() != null) {
                    onServerInputHandler.accept(objectInputStream.readObject().toString());
                }

            } catch (UnknownHostException | ConnectException e) {
                e.printStackTrace();
                onErrorHandler.accept(e.getMessage());
            } catch (EOFException e) {
                isConnectedToServer = false;
                onServerDisconnectHandler.run();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("An error occurred");
                e.printStackTrace();
            }
        }).start();
    }

    public void sendMessageToServer(String message, Runnable callback) throws IOException {
        if(isConnectedToServer) {
            objectOutputStream.writeObject(new Message(message));

            callback.run();
        }
    }

}
