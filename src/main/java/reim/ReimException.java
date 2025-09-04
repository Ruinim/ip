package reim;

/**
 * Exception class for our application
 * @author Ruinim
 */
public class ReimException extends Exception {
    /**
     * err is our error code
     * command is the command that had generated this error
     */
    private final Integer err;
    private final String command;

    /**
     * Constructor method of ReimException
     *
     * @param err error code to decide which error message to print
     * @param command the command that caused this error
     */
    public ReimException(Integer err, String command) {
        this.err = err;
        this.command = command;
    }

    /**
     * to return the error code generated
     *
     * @return error code generated
     */
    public Integer getError() {
        return this.err;
    }

    /**
     * List of possible error messages
     *
     * @return appropriate error message depending on the error code of the object
     */
    public String getErrorMessage() {
        String[] errorMsg = {"invalid command: please use the commands list, todo event, deadline, mark, unmark",
            "missing arguments", "invalid command: list command should not have arguments",
            "invalid command: mark command followed by char when it was meant to be an int",
            "Index out of bounds", "invalid arguments: no timing given", "invalid argument: no task given in command",
            "Task is already marked as not done", "Task is already marked as done", "Duplicate task",
            "Time given in wrong format", "Note: no file to read from"};
        return "Error in command: " + this.command + " ; " + errorMsg[this.err - 1];
    }

}
