package reim;

import java.util.Scanner;

/**
 * class for most of the printing done by the application
 */
public class Ui {
    private Scanner read;

    public Ui() {
        read = new Scanner(System.in);
    }


    private static String messageUiMaking(String msg) {
        return "____________________________________________________________\n"
                + msg + "\n"
                + "____________________________________________________________\n";
    }

    /**
     * printing of staring message
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
     * Checks if there is more inputs in the scanner
     *
     * @return if there is still inputs in the scanner, return true, else false
     */
    public boolean hasMoreInput() {
        if (this.read.hasNextLine()) {
            return true;
        }
        return false;
    }

    /**
     * Obtains line of input from scanner
     *
     * @return next line of scanner
     */
    public String showInputLine() {
        return this.read.nextLine();
    }

    /**
     * printing of ending message
     */
    public void end() {
        System.out.println("""
                ____________________________________________________________
                Bye. Hope to see you again soon!
                ____________________________________________________________
                """);
    }

    /**
     * Produce string needed for printing the ending statement of the application
     *
     * @return String output to be printed when the application is about to close
     */
    public String generateEndStatement() {
        return """
                ____________________________________________________________
                Bye. Hope to see you again soon!
                ____________________________________________________________
                """;
    }

    public String processErrorOutput(ReimException error) {
        return messageUiMaking(error.getErrorMessage());
    }

    public String processNormalOutput(String output) {
        return messageUiMaking(output);
    }

    /**
     * printing of normal output to be used for successful runs of commands
     *
     * @param stringToPrint output of string or command given
     */
    public void printOutput(String stringToPrint) {
        System.out.println(processNormalOutput(stringToPrint));
    }

    /**
     * printing of error outputs to be used when an error is detected
     *
     * @param errorToPrint exception object to be printed
     */
    public void printError(ReimException errorToPrint) {
        System.out.println(processErrorOutput(errorToPrint));
    }
}
