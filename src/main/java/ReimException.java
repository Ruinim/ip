public class ReimException {
    private final Integer err;

    public ReimException(Integer err) {
        this.err = err;
    }

    public String toString() {
        String[] error_msg = {"invalid command: please use the commands list, todo event, deadline, mark, unmark",
            "missing arguments", "invalid command: list command should not have arguments",
            "invalid command: mark command followed by char when it was meant to be an int",
            "Index out of bounds", "invalid arguments: no timing given", "invalid argument: no task given in command"};
        return error_msg[this.err - 1];
    }

}
