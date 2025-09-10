package reim;

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

    private Reim reim = new Reim("src/data", "src/data/reim.txt");

    public Main() throws ReimException {
    }

    /**
     * Driving function that sets up the Ui for the application
     *
     * @param stage the stage that the application will start using
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/reim/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            // inject the Duke instance
            fxmlLoader.<MainWindow>getController().setReim(reim);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
