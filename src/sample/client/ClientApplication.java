package sample.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.client.ui.ClientController;

public class ClientApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/client.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Toy World -- Client");
        primaryStage.setScene(new Scene(root, 1200, 650));
        primaryStage.setResizable(false);

        primaryStage.setOnCloseRequest(event -> System.exit(0));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
