package Reim;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private Reim reim = new Reim("src/data", "src/data/Reim.Reim.txt");

    public Main() throws ReimException {
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/Reim/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            // inject the Duke instance
            fxmlLoader.<MainWindow>getController().setDuke(reim);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
