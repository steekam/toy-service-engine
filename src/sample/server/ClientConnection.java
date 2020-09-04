package sample.server;

import sample.Protocol;
import sample.models.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class ClientConnection implements Runnable {
    private final Socket clientSocket;

    private Protocol protocol;

    private ObjectOutputStream objectOutputStream;

    private ObjectInputStream objectInputStream;

    private Consumer<String> onInputFromClientHandler;

    private Consumer<String> onClientDisconnectionHandler;

    public void setOnClientDisconnectionHandler(Consumer<String> onClientDisconnectionHandler) {
        this.onClientDisconnectionHandler = onClientDisconnectionHandler;
    }

    public ClientConnection(Socket clientSocket, Consumer<ClientConnection> onNewConnectionCallback) {
        this.clientSocket = clientSocket;
        try {
            objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        onNewConnectionCallback.accept(this);
        protocol = new Protocol();
    }

    public void setOnInputFromClientHandler(Consumer<String> onInputFromClientHandler) {
        this.onInputFromClientHandler = onInputFromClientHandler;
    }

    public String getName() {
        return "Client " + clientSocket.getPort();
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void sendRequest(String requestType) {
        try {
            protocol.currentRequestType = protocol.requestTypesMap.get(requestType);
            objectOutputStream.writeObject(new Message("Send "+requestType));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (clientSocket.getInputStream() != null) {
                objectOutputStream.writeObject(
                        protocol.processInput(objectInputStream.readObject(), onInputFromClientHandler)
                );
            }
        } catch (EOFException e) {
            onClientDisconnectionHandler.accept(getName());
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
