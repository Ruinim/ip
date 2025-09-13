package reim;

/**
 * Custom exception class for the Reim application.
 * <p>
 * Used to represent specific command-related errors during execution,
 * each with an associated error code and the offending command string.
 * </p>
 * Error messages are generated based on predefined codes and are
 * designed to provide helpful feedback to the user.
 * </p>
 *
 * @author Ruinim
 */
public class ReimException extends Exception {
    /** Error code that determines the type of exception. */
    private final Integer err;
    /** The command that triggered the exception. */
    private final String command;

    /**
     * Constructs a new ReimException with the specified error code and command.
     *
     * @param err error code to decide which error message to print
     * @param command the command that caused this error
     */
    public ReimException(Integer err, String command) {
        this.err = err;
        this.command = command;
    }

    /**
     * Returns the error code associated with this exception.
     *
     * @return An integer representing the error code.
     */
    public Integer getError() {
        return this.err;
    }

    /**
     * Returns a user-friendly error message based on the error code and offending command.
     *
     * @return A descriptive error message string for display to the user.
     */
    public String getErrorMessage() {
        String[] errorMsg = {"invalid command: please use the commands: list,"
                + "todo, event, deadline, mark, unmark, find, delete, update",
            "missing arguments", "invalid command: list command should not have arguments",
            "invalid command: mark command followed by char when it was meant to be an int",
            "Index out of bounds", "invalid arguments: no timing given", "invalid argument: no task given in command",
            "Task is already marked as not done", "Task is already marked as done", "Duplicate task",
            "Time given in wrong format", "Note: no file to read from",
            "String given cannot be converted to LocalDate/LocalTime", "Write to file failed"};
        return "Error in command: " + this.command + " ; " + errorMsg[this.err - 1];
    }

}
