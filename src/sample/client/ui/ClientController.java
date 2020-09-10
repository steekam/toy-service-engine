package sample.client.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import sample.Util;
import sample.ValidationHelper;
import sample.client.SocketClient;
import sample.models.Toy;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ClientController implements Initializable {
    private final SocketClient clientProvider;

    @FXML
    public TextArea clientLog;

    @FXML
    public JFXTextField serverHostAddress;

    @FXML
    public JFXTextField serverPort;

    @FXML
    public JFXButton connectToServerButton;

    @FXML
    public JFXRadioButton serverConnectionStatus;

    private ValidationHelper serverConnectionValidator;

    private Toy toy;

    @FXML
    private JFXTextField codeField;

    @FXML
    private JFXTextField nameField;

    @FXML
    private JFXTextField descriptionField;

    @FXML
    private JFXTextField priceField;

    @FXML
    private JFXTextField batchNumberField;

    @FXML
    private JFXDatePicker dateOfManufacture;

    @FXML
    private JFXTextField companyNameField;

    @FXML
    private JFXTextField streetAddressField;

    @FXML
    private JFXTextField zipCodeField;

    @FXML
    private JFXTextField countryField;

    @FXML
    private JFXButton clearAllButton;

    @FXML
    private JFXButton submitToServerButton;

    public ClientController() {
        clientProvider = new SocketClient();

        clientProvider.setOnServerInputHandler(body -> Platform.runLater(() -> printToClientLog(body) ));

        clientProvider.setOnErrorHandler((this::printToClientLog));

        clientProvider.setOnServerDisconnectHandler(() -> {
            serverConnectionStatus.setSelected(false);
            printToClientLog("The server connection has been lost");
        });
    }

    private void initServerConnectionValidator() {
        serverConnectionValidator = new ValidationHelper(
                new HashMap<>(
                        Map.ofEntries(
                                Map.entry(serverHostAddress, Collections.singletonList("required")),
                                Map.entry(serverPort, Arrays.asList("required", "number")))
                )
        );
    }

    public void connectToServer() {
        if (!serverConnectionValidator.validate()) return;

        printToClientLog(String.format("Connecting to %s:%s", serverHostAddress.getText(), serverPort.getText()));

        if (!clientProvider.isConnectedToServer()) {
            clientProvider.connectToServer(
                    serverHostAddress.getText(), Integer.parseInt(serverPort.getText()),
                    () -> Platform.runLater(() -> {
                        serverConnectionStatus.setSelected(true);

                        serverConnectionValidator.clearAllFields();
                    })
            );
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initServerConnectionValidator();

        bindToyFormValues();
    }

    private void bindToyFormValues() {
        toy = new Toy();

        codeField.textProperty().bindBidirectional(toy.codeProperty());
        nameField.textProperty().bindBidirectional(toy.nameProperty());
        descriptionField.textProperty().bindBidirectional(toy.descriptionProperty());
        priceField.textProperty().bindBidirectional(toy.priceProperty());
        batchNumberField.textProperty().bindBidirectional(toy.batchNumberProperty());
        companyNameField.textProperty().bindBidirectional(toy.getManufacturer().companyNameProperty());
        streetAddressField.textProperty().bindBidirectional(toy.getManufacturer().streetAddressProperty());
        zipCodeField.textProperty().bindBidirectional(toy.getManufacturer().zipCodeProperty());
        countryField.textProperty().bindBidirectional(toy.getManufacturer().countryProperty());

        dateOfManufacture.valueProperty().addListener(((observableValue, oldDate, newDate) -> {
            toy.setDateOfManufacture(newDate == null ? null : newDate.toString());
        }));
    }

    public void clearAll() {
        toy.resetValues();
        dateOfManufacture.setValue(null);
    }

    public void sayThankYou() {
        try {
            clientProvider.sendMessageToServer(String.format("Thank you <%s>", UUID.randomUUID()), () -> printToClientLog("Sending Thank you message to server"));
        } catch (IOException exception) {
            printToClientLog("Error: Could not send thank you message.");
            exception.printStackTrace();
        }
    }

    public void submitToServer() {
        try {
            clientProvider.sendMessageToServer(toy.toString(), () -> printToClientLog("Sending: "+toy.toString()));
        } catch (IOException exception) {
            printToClientLog("Error: Could not send toy details.");
            exception.printStackTrace();
        }
    }

    private void printToClientLog(String message) {
        clientLog.appendText(String.format("[%s] %s \n", Util.currentDateTime(), message));
    }
}
