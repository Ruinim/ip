package Reim;

/**
 * class for most of the printing done by the application
 */
public class Ui {

    public Ui() {
    }


    private static String message(String msg) {
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
     * printing of ending message
     */
    public void end() {
        System.out.println("""
                ____________________________________________________________
                Bye. Hope to see you again soon!
                ____________________________________________________________
                """);
    }

    private static String errorOutput(ReimException e) {
        return message(e.errorMessage());
    }

    private static String normalOutput(String s) {
        return message(s);
    }

    /**
     * printing of normal output
     * @param s output of string or command given
     */
    public void printOutput(String s) {
        System.out.println(normalOutput(s));
    }

    /**
     * printing of error outputs
     * @param e exception object to be printed
     */
    public void printError(ReimException e) {
        System.out.println(errorOutput(e));
    }
}
