package sample.server.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import sample.Util;
import sample.server.ClientConnection;
import sample.server.SocketServer;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    private final SocketServer toyServiceServer;
    @FXML
    public JFXToggleButton toggleConnectionButton;
    @FXML
    public JFXTextArea serverLog;
    @FXML
    public JFXComboBox<Label> requestTypeOptions;
    @FXML
    public JFXComboBox<Label> connectedClients;
    @FXML
    public JFXButton sendClientRequestButton;

    public ServerController() {
        toyServiceServer = new SocketServer();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initRequestTypeOptions();

        initSendRequestValidator();

        // Set handler for new connection
        toyServiceServer.setOnNewConnectionCallback(this::onNewConnectionHandler);
    }

    private void onNewConnectionHandler(ClientConnection clientConnection) {
        printToServerLog("New client connected on " + clientConnection.getClientSocket().getRemoteSocketAddress());

        // On new input handler
        clientConnection.setOnInputFromClientHandler(messageString ->
                printToServerLog(String.format("From [%s] => %s", clientConnection.getName(), messageString))
        );

        // On client disconnection handler
        clientConnection.setOnClientDisconnectionHandler(this::removeClientFromOptions);

        connectedClients.getItems().add(new Label(clientConnection.getName()));
    }

    private void removeClientFromOptions(String clientName) {
        printToServerLog(clientName + " has disconnected.");

        toyServiceServer.removeDisconnectedClient(clientName);

        connectedClients.getItems().setAll(
                connectedClients.getItems().stream().filter( label -> !label.getText().equals(clientName))
                .toArray(Label[]::new)
        );
    }

    private void initRequestTypeOptions() {
        requestTypeOptions.getItems().addAll(
                Util.requestTypes()
                        .stream().map(Label::new)
                        .toArray(Label[]::new)
        );
    }

    private void initSendRequestValidator() {
    }

    public void toggleServerConnection() {
        if (toyServiceServer.isConnected()) {
            toyServiceServer.disconnect(() -> {
                Platform.runLater(() -> {
                    toggleConnectionButton.setText("Disconnected");
                    toggleConnectionButton.setSelected(false);
                });
                printToServerLog("Server has been disconnected.");
            });
            return;
        }

        toyServiceServer.start((connectedPort) -> {
            Platform.runLater(() -> {
                toggleConnectionButton.setText("Connected (Port " + connectedPort + ")");
                toggleConnectionButton.setSelected(true);
            });
            printToServerLog("Server connected: Port " + connectedPort);
            printToServerLog("Waiting for clients to connect ...");
        });
    }

    private void printToServerLog(String message) {
        serverLog.appendText(String.format("[%s] %s \n", Util.currentDateTime(), message));
    }

    /**
     * Returns true if form has errors
     *
     * @return boolean
     */
    private boolean validateRequestForm() {

        return (requestTypeOptions.getSelectionModel().getSelectedItem() == null) || (connectedClients.getSelectionModel().getSelectedItem() == null);
    }

    private void resetForm() {
        connectedClients.getSelectionModel().clearSelection();
        requestTypeOptions.getSelectionModel().clearSelection();
    }

    public void sendClientRequest() {
        if (validateRequestForm()) {
            new Alert(Alert.AlertType.ERROR, "All fields are required")
                    .showAndWait();
            return;
        }

        toyServiceServer.sendRequestToClient(
                connectedClients.getValue().getText(),
                requestTypeOptions.getValue().getText(),
                () -> printToServerLog("Sending request to " + connectedClients.getValue().getText())
        );

        resetForm();
    }
}
