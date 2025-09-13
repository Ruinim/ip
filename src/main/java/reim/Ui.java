package reim;

import java.util.Scanner;

/**
 * Handles all user interaction for the Reim application.
 * <p>
 * This class manages the input/output interface through the console.
 * It prints welcome and exit messages, outputs command results,
 * and handles error messages consistently with formatting.
 * </p>
 *
 * @author Ruinim
 */
public class Ui {
    private Scanner read;

    /**
     * Constructs a Ui instance with an input scanner.
     */
    public Ui() {
        read = new Scanner(System.in);
    }


    /**
     * Formats a message with a surrounding border for consistent UI output.
     *
     * @param msg The message to format.
     * @return A string wrapped with horizontal borders.
     */
    private static String makeUiMessage(String msg) {
        return "____________________________________________________________\n"
                + msg + "\n"
                + "____________________________________________________________\n";
    }

    /**
     * Prints the startup message when the application begins.
     */
    public void start() {
        System.out.println("""
                ____________________________________________________________
                Hello! I'm Reim
                What can I do for you?
                ____________________________________________________________
                """);
    }

    /**
     * Checks if there is another line of input available.
     *
     * @return true if input is available; false otherwise.
     */
    public boolean hasMoreInput() {
        if (this.read.hasNextLine()) {
            return true;
        }
        return false;
    }

    /**
     * Reads and returns the next line of user input.
     *
     * @return The next line of input from the user.
     */
    public String showInputLine() {
        return this.read.nextLine();
    }

    /**
     * Prints the farewell message when the application ends.
     */
    public void end() {
        System.out.println("""
                ____________________________________________________________
                Bye. Hope to see you again soon!
                ____________________________________________________________
                """);
    }

    /**
     * Generates the formatted exit message.
     *
     * @return A string representing the exit message.
     */
    public String generateEndStatement() {
        return """
                ____________________________________________________________
                Bye. Hope to see you again soon!
                ____________________________________________________________
                """;
    }

    /**
     * Formats an error message for display.
     *
     * @param error The ReimException containing the error details.
     * @return A formatted error message string.
     */
    public String processErrorOutput(ReimException error) {
        return makeUiMessage(error.getErrorMessage());
    }

    /**
     * Formats a normal message (e.g., success or status messages) for display.
     *
     * @param output The message to display.
     * @return A formatted message string.
     */
    public String processNormalOutput(String output) {
        return makeUiMessage(output);
    }

    /**
     * Prints a formatted message to the console.
     * Typically used for command results or informational messages.
     *
     * @param stringToPrint The message to print.
     */
    public void printOutput(String stringToPrint) {
        System.out.println(processNormalOutput(stringToPrint));
    }

    /**
     * Prints a formatted error message to the console.
     *
     * @param errorToPrint The {@code ReimException} to display.
     */
    public void printError(ReimException errorToPrint) {
        System.out.println(processErrorOutput(errorToPrint));
    }
}
