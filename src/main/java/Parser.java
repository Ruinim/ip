import java.util.ArrayList;

public class Parser {
    protected String command;
    protected TaskList tasks;

    public Parser(String command, TaskList tasks) {
        this.command = command;
        this.tasks = tasks;
    }

    public String addingList() {
        if (this.command.startsWith("todo")) {
            this.tasks.add(new Todo("[ ]", this.command.substring(5)));
            return new Todo("[ ]", this.command.substring(5)).toString();
        }
        else if (this.command.startsWith("deadline")) {
            int index = this.command.indexOf("/");
            String task = this.command.substring(9, index);
            String deadline = this.command.substring(index+ 3);
            this.tasks.add(new Deadline("[ ]", task, deadline));
            return new Deadline("[ ]", task, deadline).toString();
        }
        else if (this.command.startsWith("event")) {
            int index = this.command.indexOf("/");
            String task = this.command.substring(6, index);
            String at = this.command.substring(index + 5);
            this.tasks.add(new Event("[ ]", task, at));
            return new Event("[ ]", task, at).toString();
        }
        return "";
    }

    public String action() {
        Integer commandType = commandParse(this.command);
        String finalOutput = "";
        if (commandType.equals(2)) { //list
            finalOutput = listOutput(this.tasks);
        }
        else if (commandType.equals(3)) { //mark
            String taskIndex = this.command.substring(5); //number
            Task t = this.tasks.get(Integer.parseInt(taskIndex) - 1);
            this.tasks.set(Integer.parseInt(taskIndex) - 1, t.mark());
            finalOutput = "Nice! I've marked this task as done:\n" + t.mark();
        }
        else if (commandType.equals(4)) { //unmark
            String taskIndex = this.command.substring(7); //number
            Task t = this.tasks.get(Integer.parseInt(taskIndex) - 1);
            this.tasks.set(Integer.parseInt(taskIndex) - 1, t.unmark());
            finalOutput = "OK, I've marked this task as not done yet:\n" + t.unmark();
        }
        else if (commandType.equals(5)) { //delete
            String taskIndex = this.command.substring(7);
            Task t = this.tasks.get(Integer.parseInt(taskIndex) - 1);
            this.tasks.remove(Integer.parseInt(taskIndex) - 1);
            finalOutput = "Noted, I've removed this task:\n" + t +"\nNow you have "
                    + this.tasks.size() + " task(s) in the list";
        }
        return finalOutput;
    }

    private static Integer commandParse(String command) {
        if (command.equals("list")) {
            return 2;
        }
        else if (command.startsWith("mark")) {
            return 3;
        }
        else if (command.startsWith("unmark")) {
            return 4;
        }
        else if (command.startsWith("delete")) {
            return 5;
        }
        return 0;
    }

    private static String listOutput(TaskList arr) {
        StringBuilder finalString = new StringBuilder();
        for (int i = 1; i - 1 < arr.size(); i++) {
            finalString.append(i).append(". ").append(arr.get(i).toString()).append("\n");
        }
        return finalString.toString();
    }

    public Integer errorInCommand() {
        // list, todo, event, deadline, mark, unmark
        String[] command_list = {"list", "todo", "deadline", "event", "mark", "unmark", "delete"};
        int error_code = 0;
        int count = 0;
        for (int i = 0; i < command_list.length; i++) {
            if (this.command.startsWith(command_list[i])) {
                count++;
                if (!this.command.equals("list") && this.command.length() < command_list[i].length() + 2) {
                    return 2; // missing arguments
                }
                else if (this.command.startsWith("list") && this.command.length() > 4) {
                    return 3; //invalid command: list command does not have arguments
                }
                else if (this.command.startsWith("mark")) {
                    error_code = markCheck(this.command, this.tasks, error_code);
                }
                else if (this.command.startsWith("unmark")) {
                    error_code = unmarkCheck(this.command, this.tasks, error_code);
                }
                else if (this.command.startsWith("delete")) {
                    error_code = deleteCheck(this.command, this.tasks, error_code);
                }
                else if (this.command.startsWith("todo")) {
                    error_code = todoCheck(this.command, this.tasks);
                }
                else if (this.command.startsWith("deadline")) {
                    error_code = deadlineCheck(this.command, this.tasks);
                }
                else if (this.command.startsWith("event")) {
                    error_code = eventCheck(this.command, this.tasks);
                }

            }
        }
        if (count < 1) {
            return 1; //invalid command: please use the commands list, todo event, deadline, mark, unmark
        }
        return error_code;
    }

    private static Integer markCheck(String command, TaskList arr, Integer error_code) {
        try {
            String taskIndex = command.substring(5); //number
            Task t = arr.get(taskIndex);
            if (t.getDone().equals("[X]")) {
                error_code = 9; // task is already marked as not done
            }
        } catch (NumberFormatException e) {
            error_code = 4; // invalid command: mark command followed by char when it was meant to be an int
        } catch (IndexOutOfBoundsException e) {
            error_code = 5; // Index out of bounds
        }
        return error_code;
    }

    private static Integer unmarkCheck(String command, TaskList arr, Integer error_code) {
        try {
            String taskIndex = command.substring(7); //number
            Task t = arr.get(taskIndex);
            if (t.getDone().equals("[ ]")) {
                error_code = 8; // task is already marked as not done
            }
        } catch (NumberFormatException e) {
            error_code = 4; // invalid command: mark command followed by char when it was meant to be an int
        } catch (IndexOutOfBoundsException e) {
            error_code = 5; // Index out of bounds
        }

        return error_code;
    }

    private static Integer todoCheck(String command, TaskList arr) {
        if (arr.getArray().stream().anyMatch(x -> x.getTask().equals(command.substring(5)))) {
            return 10; //duplicate task
        }
        return 0;
    }

    private static Integer deadlineCheck(String command, TaskList arr) {
        if(!command.contains("/by")) { //no /by
            return 6; // invalid arguments: no timing given
        }
        int index = command.indexOf("/");
        if (command.substring(9, index).isEmpty()) {
            return 7; // invalid argument: no task given in command
        }
        else if (command.substring(index+ 3).isEmpty()) {
            return 6;
        }
        else if (arr.getArray().stream().anyMatch(x -> x.getTask().equals(command.substring(9,index)))) {
            return 10; //duplicate task
        }
        return 0;
    }

    private static Integer eventCheck(String command, TaskList arr) {
        if (!command.contains("/from")) {
            return 6; // invalid arguments: no timing given
        }
        int index = command.indexOf("/");
        if (command.substring(6, index).isEmpty()) {
            return 7; // invalid argument: no task given in command
        }
        else if (command.substring(index + 5).isEmpty()) {
            return 6;
        }
        else if (arr.getArray().stream().anyMatch(x -> x.getTask().equals(command.substring(6,index)))) {
            return 10; // duplicate task
        }
        return 0;
    }

    private static Integer deleteCheck(String command, TaskList arr, Integer error_code) {
        try {
            String taskIndex = command.substring(7); //number
            Task t = arr.get(taskIndex);
        } catch (NumberFormatException e) {
            error_code = 4; // invalid command: mark command followed by char when it was meant to be an int
        } catch (IndexOutOfBoundsException e) {
            error_code = 5; // Index out of bounds
        }
        return error_code;
    }
}
