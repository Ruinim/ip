package Reim;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Parser is the main class where we check if the command given is valid and to take action on the command afterwards
 * @author Ruinim
 */
public class Parser {
    /**
     * command is the command given that Parser needs to check the validity of
     * tasks is the TaskList of Tasks that we are to use tot check if the command is valid
     */
    private final String command;
    private TaskList tasks;

    public Parser(String command, TaskList tasks) {
        this.command = command;
        this.tasks = tasks;
    }

    /**
     * processes the task given in the command and adds it to the TaskList
     * @return string output of the new task to be added to the tasklist
     * */
    public String addingList() {
        if (this.command.startsWith("todo")) {
            this.tasks.add(new Todo("[ ]", this.command.substring(5)));
            return new Todo("[ ]", this.command.substring(5)).toString();
        }
        else if (this.command.startsWith("deadline")) {
            int index = this.command.indexOf("/");
            String task = this.command.substring(9, index - 1);
            String deadline = this.command.substring(index+ 4);
            if (deadline.length() == 15) { // yyyy-mm-dd tttt
                LocalDate date = LocalDate.parse(deadline.substring(0, 10));
                String timing = deadline.substring(11);
                String formatted_timing = new StringBuilder(timing).insert(2, ":").toString();
                LocalTime time = LocalTime.parse(formatted_timing);
                this.tasks.add(new Deadline("[ ]", task, date, time));
                return new Deadline("[ ]", task, date, time).toString();
            } else {
                LocalDate date = LocalDate.parse(deadline);
                this.tasks.add(new Deadline("[ ]", task, date));
                return new Deadline("[ ]", task, date).toString();
            }
        }
        else if (this.command.startsWith("event")) {
            int index = this.command.indexOf("/");
            String task = this.command.substring(6, index - 1);
            String at = this.command.substring(index + 6);
            if (at.length() == 15) { // yyyy-mm-dd tttt
                LocalDate date = LocalDate.parse(at.substring(0, 10));
                String timing = at.substring(11);
                String formatted_timing = new StringBuilder(timing).insert(2, ":").toString();
                LocalTime time = LocalTime.parse(formatted_timing);
                this.tasks.add(new Event("[ ]", task, date, time));
                return new Event("[ ]", task, date, time).toString();
            }
            LocalDate date = LocalDate.parse(at);
            this.tasks.add(new Event("[ ]", task, date));
            return new Event("[ ]", task, date).toString();
        }
        return "";
    }

    /**
     * Acts on the command, acting differently depending on the different commands without addition of new tasks to the TaskList
     * @return string output of the command
     */
    public String action() {
        Integer commandType = commandParse(this.command);
        String finalOutput = "";
        if (commandType.equals(2)) { //list
            finalOutput = listOutput(this.tasks);
        } else if (commandType.equals(3)) { //mark
            String taskIndex = this.command.substring(5); //number
            Task t = this.tasks.get(Integer.parseInt(taskIndex) - 1);
            this.tasks.set(Integer.parseInt(taskIndex) - 1, t.mark());
            finalOutput = "Nice! I've marked this task as done:\n" + t.mark();
        } else if (commandType.equals(4)) { //unmark
            String taskIndex = this.command.substring(7); //number
            Task t = this.tasks.get(Integer.parseInt(taskIndex) - 1);
            this.tasks.set(Integer.parseInt(taskIndex) - 1, t.unmark());
            finalOutput = "OK, I've marked this task as not done yet:\n" + t.unmark();
        } else if (commandType.equals(5)) { //delete
            String taskIndex = this.command.substring(7);
            Task t = this.tasks.get(Integer.parseInt(taskIndex) - 1);
            this.tasks.remove(Integer.parseInt(taskIndex) - 1);
            finalOutput = "Noted, I've removed this task:\n" + t +"\nNow you have "
                    + this.tasks.size() + " task(s) in the list";
        } else if (commandType.equals(6)) {
            String searchCriteria = this.command.substring(5);
            TaskList t = this.tasks.searchList(searchCriteria);
            if (t.size() == 0) {
                finalOutput = "There were no matches in your list\n";
            } else {
                finalOutput = "Here are the matching tasks in your list:\n" + listOutput(t);
            }
        }
        return finalOutput;
    }

    /**
     * check which command is to be called (list, mark, unmark, delete)
     * @param command is the command to be checked
     * @return An Integer respective to the different possible commands
     */
    private static Integer commandParse(String command) {
        if (command.equals("list")) {
            return 2;
        } else if (command.startsWith("mark")) {
            return 3;
        } else if (command.startsWith("unmark")) {
            return 4;
        } else if (command.startsWith("delete")) {
            return 5;
        } else if (command.startsWith("find")) {
            return 6;
        }
        return 0;
    }

    /**
     * Lists the entries of the current TaskList
     * @param arr the TaskList to be printed
     * @return String output of the entries of the TaskList
     */
    private static String listOutput(TaskList arr) {
        StringBuilder finalString = new StringBuilder();
        for (int i = 1; i - 1 < arr.size(); i++) {
            finalString.append(i).append(". ").append(arr.get(i - 1).toString()).append("\n");
        }
        return finalString.toString();
    }

    /**
     * Check for any errors in the command that would cause it to be invalid
     * @return error code integer depending on what error has been detected (0 for no error)
     */
    public Integer errorInCommand() {
        // list, todo, event, deadline, mark, unmark
        String[] command_list = {"list", "todo", "deadline", "event", "mark", "unmark", "delete", "find"};
        int error_code = 0;
        int count = 0;
        for (int i = 0; i < command_list.length; i++) {
            if (this.command.startsWith(command_list[i])) {
                count++;
                if (!this.command.equals("list") && this.command.length() < command_list[i].length() + 2) {
                    return 2; // missing arguments
                } else if (this.command.startsWith("list") && this.command.length() > 4) {
                    return 3; //invalid command: list command does not have arguments
                } else if (this.command.startsWith("mark")) {
                    error_code = markCheck(this.command, this.tasks, error_code);
                } else if (this.command.startsWith("unmark")) {
                    error_code = unmarkCheck(this.command, this.tasks, error_code);
                } else if (this.command.startsWith("delete")) {
                    error_code = deleteCheck(this.command, this.tasks, error_code);
                } else if (this.command.startsWith("todo")) {
                    error_code = todoCheck(this.command, this.tasks);
                } else if (this.command.startsWith("deadline")) {
                    error_code = deadlineCheck(this.command, this.tasks);
                } else if (this.command.startsWith("event")) {
                    error_code = eventCheck(this.command, this.tasks);
                }

            }
        }
        if (count < 1) {
            return 1; //invalid command: please use the commands list, todo event, deadline, mark, unmark
        }
        return error_code;
    }

    /**
     * checks for errors relating to the "mark" command
     * @param command command to be checked
     * @param arr the current tasklist
     * @param error_code error code to be returned
     * @return error code pertaining to the error detected
     */
    private static Integer markCheck(String command, TaskList arr, Integer error_code){
        try {
            String taskIndex = command.substring(5); //number
            if (cannotIntParse(taskIndex)) {
                error_code = 4;
                throw new ReimException(4,command);
            }
            int index = Integer.parseInt(taskIndex);
            if (index > arr.size()) {
                error_code = 5;
                throw new ReimException(5, command);
            }
            Task t = arr.get(index - 1);
            if (t.getDone().equals("[X]")) {
                error_code = 9; // task is already marked as done
                throw new ReimException(9, command);
            }
        } catch (ReimException e) {
            return error_code;
        }
        return error_code;
    }

    /**
     * Checking if a string can be converted to an integer
     * @param s string to be tested
     * @return false if s can be converted to integer, else true
     */
    private static boolean cannotIntParse(String s) {
        try {
            Integer.parseInt(s);
            return false;
        }
        catch (NumberFormatException e) {
            return true;
        }
    }

    /**
     * checks for errors relating to the "unmark" command
     * @param command command to be checked
     * @param arr the current tasklist
     * @param error_code error code to be returned
     * @return error code pertaining to the error detected
     */
    private static Integer unmarkCheck(String command, TaskList arr, Integer error_code) {
        try {
            String taskIndex = command.substring(7); //number
            if (cannotIntParse(taskIndex)) {
                error_code = 4;
                throw new ReimException(4,command);
            }
            int index = Integer.parseInt(taskIndex);
            if (index > arr.size()) {
                error_code = 5;
                throw new ReimException(5, command);
            }
            Task t = arr.get(index - 1);
            if (t.getDone().equals("[ ]")) {
                error_code = 8; // task is already marked as not done
                throw new ReimException(9, command);
            }
        } catch (ReimException e) {
            return error_code;
        }
        return error_code;
    }

    /**
     * checks for errors relating to the "todo" command
     * @param command command to be checked
     * @param arr the current tasklist
     * @return error code pertaining to the error detected
     */
    private static Integer todoCheck(String command, TaskList arr) {
        try {
            if (arr.getArray().stream().anyMatch(x -> x.getTask().equals(command.substring(5)))) {
                //duplicate task
                throw new ReimException(10, command);
            }
        } catch (ReimException e) {
            return 10;
        }
        return 0;
    }

    /**
     * checks for errors relating to the "deadline" command
     * @param command command to be checked
     * @param arr the current tasklist
     * @return error code pertaining to the error detected
     */
    private static Integer deadlineCheck(String command, TaskList arr) {
        try {
            if (!command.contains("/by")) { //no /by
                throw new ReimException(6, command); // invalid arguments: no timing given
            }
            int index = command.indexOf("/");
            if (command.substring(9, index).isEmpty()) {
                throw new ReimException(7, command); // invalid argument: no task given in command
            } else if (command.substring(index + 3).isEmpty()) {
                throw new ReimException(6, command);
            } else if (arr.getArray().stream().anyMatch(x -> x.getTask().equals(command.substring(9, index)))) {
                throw new ReimException(10, command); //duplicate task
            }
            else if (!(command.substring(index + 4).matches("\\d{4}-\\d{2}-\\d{2} \\d{4}") ||
                    command.substring(index + 4).matches("\\d{4}-\\d{2}-\\d{2}"))) {
                throw new ReimException(11, command);
            }
//            else if (Integer.parseInt(command.substring(index + 9, index + 11)) > 12 || Integer.parseInt(command.substring(index + 13, index + 15)) > 31) {
//                throw new ReimException(12, command);
//            }
        } catch (ReimException e) {
            return e.getError();
        }
        return 0;

    }

    /**
     * checks for errors relating to the "event" command
     * @param command command to be checked
     * @param arr the current tasklist
     * @return error code pertaining to the error detected
     */
    private static Integer eventCheck(String command, TaskList arr) {
        try {
            if (!command.contains("/from")) {
                throw new ReimException(6, command); // invalid arguments: no timing given
            }
            int index = command.indexOf("/");
            if (command.substring(6, index).isEmpty()) {
                throw new ReimException(7, command); // invalid argument: no task given in command
            } else if (command.substring(index + 5).isEmpty()) {
                throw new ReimException(6, command);
            } else if (arr.getArray().stream().anyMatch(x -> x.getTask().equals(command.substring(6, index)))) {
                throw new ReimException(10, command); // duplicate task
            } else if (!(command.substring(index + 6).matches("\\d{4}-\\d{2}-\\d{2} \\d{4}") ||
                    command.substring(index + 6).matches("\\d{4}-\\d{2}-\\d{2}"))) {
                throw new ReimException(11, command);
            }
        }
        catch (ReimException e) {
            return e.getError();
        }
        return 0;
    }

    /**
     * checks for errors relating to the "delete" command
     * @param command command to be checked
     * @param arr the current tasklist
     * @param error_code error code to be returned
     * @return error code pertaining to the error detected
     */
    private static Integer deleteCheck(String command, TaskList arr, Integer error_code) {
        try {
            String taskIndex = command.substring(7); //number
            if (cannotIntParse(taskIndex)) {
                throw new ReimException(4, command);
            }
            int index = Integer.parseInt(taskIndex);
            if (index > arr.size() || index <= 0) {
                throw new ReimException(5, command);
            }
        } catch (ReimException e) {
            return e.getError(); // invalid command: mark command followed by char when it was meant to be an int
        }
        return error_code;
    }
}
