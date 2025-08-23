package Reim;

public class ReimException extends Exception{
    private final Integer err;
    private final String command;

    public ReimException(Integer err, String command) {
        this.err = err;
        this.command = command;
    }
//    public Reim.ReimException() {
//
//    }
    public Integer getError() {
        return this.err;
    }


    public String errorMessage() {
        String[] error_msg = {"invalid command: please use the commands list, todo event, deadline, mark, unmark",
            "missing arguments", "invalid command: list command should not have arguments",
            "invalid command: mark command followed by char when it was meant to be an int",
            "Index out of bounds", "invalid arguments: no timing given", "invalid argument: no task given in command",
            "Reim.Task is already marked as not done", "Reim.Task is already marked as done", "Duplicate task", "Time given in wrong format",
            "Invalid date"};
        return "Error in command: " + this.command + " ; " + error_msg[this.err - 1];
    }

}
