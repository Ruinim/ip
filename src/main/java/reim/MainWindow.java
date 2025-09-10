package reim;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Reim reim;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/reim/images/DaUser.png"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/reim/images/DaDuke.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the Reim instance
     *
     * @param r the Reim instance to inject
     */
    public void setReim(Reim r) {
        reim = r;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = reim.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getReimDialog(response, dukeImage)
        );
        if (response.equals("""
                ____________________________________________________________
                Bye. Hope to see you again soon!
                ____________________________________________________________
                """)) {
            System.exit(0);
        }
        userInput.clear();
    }
}
