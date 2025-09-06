package reim;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Parser is the main class where we check if the command given is valid and to take action on the command afterwards
 * @author Ruinim
 */
public class Parser {
    static final Integer NO_ERROR = 0;
    static final Integer NO_COMMAND = 1;
    static final Integer MISSING_ARG = 2;
    static final Integer LIST_WITH_TAIL = 3;
    static final Integer NO_FOLLOWING_INT = 4;
    static final Integer INDEX_OUT_OF_BOUNDS = 5;
    static final Integer NO_TIMING_GIVEN = 6;
    static final Integer NO_TASK_GIVEN = 7;
    static final Integer TASK_ALREADY_NOT_DONE = 8;
    static final Integer TASK_ALREADY_DONE = 9;
    static final Integer DUPLICATE_TASK = 10;
    static final Integer TIME_WRONG_FORMAT = 11;
    static final Integer CANNOT_CONVERT_TO_LOCALDATETIME = 13;
    /**
     * command is the command given that Parser needs to check the validity of
     * tasks is the TaskList of Tasks that we are to use tot check if the command is valid
     */
    private final String command;
    private TaskList tasks;

    /**
     * Constructor method of Parser
     *
     * @param command String of command to parse
     * @param tasks TaskList of tasks to check from
     */
    public Parser(String command, TaskList tasks) {
        this.command = command;
        this.tasks = tasks;
    }

    /**
     * processes the task given in the command and adds it to the TaskList
     *
     * @return string output of the new task to be added to the tasklist
     * */
    public String addList() {
        if (this.command.startsWith("todo")) {
            return addToDoToList(this.command, this.tasks);
        } else if (this.command.startsWith("deadline")) {
            return addDeadlineToList(this.command, this.tasks);
        } else if (this.command.startsWith("event")) {
            return addEventToList(this.command, this.tasks);
        }
        return "";
    }

    private static String addToDoToList(String command, TaskList tasks) {
        tasks.add(new Todo(false, command.substring(5)));
        return new Todo(false, command.substring(5)).toString();
    }

    private static String addDeadlineToList(String command, TaskList tasks) {
        int index = command.indexOf("/");
        String task = command.substring(9, index - 1);
        String deadline = command.substring(index + 4);
        if (deadline.length() == 15) { // yyyy-mm-dd tttt
            String dateString = deadline.substring(0, 10);
            String timingString = deadline.substring(11);
            String formattedTiming = new StringBuilder(timingString).insert(2, ":").toString();

            LocalDate date = LocalDate.parse(dateString);
            LocalTime time = LocalTime.parse(formattedTiming);
            tasks.add(new Deadline(false, task, date, time));
            return new Deadline(false, task, date, time).toString();

        } else {
            LocalDate date = LocalDate.parse(deadline);
            tasks.add(new Deadline(false, task, date));
            return new Deadline(false, task, date).toString();
        }
    }

    private static String addEventToList(String command, TaskList tasks) {
        int index = command.indexOf("/");
        String task = command.substring(6, index - 1);
        String at = command.substring(index + 6);
        if (at.length() == 15) { // yyyy-mm-dd tttt
            String dateString = at.substring(0, 10);
            String timingString = at.substring(11);
            String formattedTiming = new StringBuilder(timingString).insert(2, ":").toString();

            LocalDate date = LocalDate.parse(dateString);
            LocalTime time = LocalTime.parse(formattedTiming);
            tasks.add(new Event(false, task, date, time));
            return new Event(false, task, date, time).toString();
        }
        LocalDate date = LocalDate.parse(at);
        tasks.add(new Event(false, task, date));
        return new Event(false, task, date).toString();
    }

    private static boolean canStringConvertToLocalDate(String dateString) {
        try {
            LocalDate time = LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    private static boolean canStringConvertToLocalTime(String timeString) {
        try {
            LocalTime time = LocalTime.parse(timeString);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Acts on the command, acting differently depending on the different
     * commands without addition of new tasks to the TaskList
     *
     * @return string output of the command
     */
    public String action() {
        Integer commandType = commandParse(this.command);
        String finalOutput = "";
        assert commandType > 0 && commandType < 7;
        if (commandType.equals(2)) { //list
            finalOutput = showListOutput(this.tasks);
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
            finalOutput = "Noted, I've removed this task:\n" + t + "\nNow you have "
                    + this.tasks.getSize() + " task(s) in the list";
        } else if (commandType.equals(6)) {
            String searchCriteria = this.command.substring(5);
            TaskList t = this.tasks.searchList(searchCriteria);
            if (t.getSize() == 0) {
                finalOutput = "There were no matches in your list\n";
            } else {
                finalOutput = "Here are the matching tasks in your list:\n" + showListOutput(t);
            }
        }
        return finalOutput;
    }

    /**
     * Checks which command is to be called (list, mark, unmark, delete) and returns the correct integer
     *
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
        return 1;
    }

    /**
     * Lists the entries of the current TaskList for it to be printed
     *
     * @param arr the TaskList to be printed
     * @return String output of the entries of the TaskList
     */
    private static String showListOutput(TaskList arr) {
        StringBuilder finalString = new StringBuilder();
        for (int i = 1; i - 1 < arr.getSize(); i++) {
            finalString.append(i).append(". ").append(arr.get(i - 1).toString()).append("\n");
        }
        String finalOutput = finalString.toString();
        if (finalOutput.isEmpty()) {
            return "The list is currently empty";
        }
        return finalString.toString();
    }

    /**
     * Check for any errors in the command that would cause it to be invalid from the possible commands of list,
     * todo, deadline, event, unmark , delete and find
     *
     * @return error code integer depending on what error has been detected (0 for no error)
     */
    public Integer errorInCommand() {
        // list, todo, event, deadline, mark, unmark
        String[] commandList = {"list", "todo", "deadline", "event", "mark", "unmark", "delete", "find"};
        int errorCode = 0;
        boolean isACommand = false;
        for (int i = 0; i < commandList.length; i++) {
            if (this.command.startsWith(commandList[i])) {
                isACommand = true;
                if (!this.command.equals("list") && this.command.length() < commandList[i].length() + 2) {
                    return MISSING_ARG; // missing arguments
                } else if (this.command.startsWith("list") && this.command.length() > 4) {
                    return LIST_WITH_TAIL; //invalid command: list command does not have arguments
                } else if (this.command.startsWith("mark")) {
                    errorCode = checkMarkCommand(this.command, this.tasks, errorCode);
                } else if (this.command.startsWith("unmark")) {
                    errorCode = checkUnmarkCommand(this.command, this.tasks, errorCode);
                } else if (this.command.startsWith("delete")) {
                    errorCode = checkDeleteCommand(this.command, this.tasks, errorCode);
                } else if (this.command.startsWith("todo")) {
                    errorCode = checkToDoCommand(this.command, this.tasks);
                } else if (this.command.startsWith("deadline")) {
                    errorCode = checkDeadlineCommand(this.command, this.tasks);
                } else if (this.command.startsWith("event")) {
                    errorCode = checkEventCommand(this.command, this.tasks);
                }

            }
        }
        if (!isACommand) {
            return NO_COMMAND; //invalid command: please use the commands list, todo event, deadline, mark, unmark
        }
        return errorCode;
    }

    /**
     * checks for errors relating to the "mark" command (cannot convert index string to integer,
     * index out of index of TaskList, Task already marked as done)
     *
     * @param command command to be checked
     * @param arr the current tasklist
     * @param errorCode error code to be returned
     * @return error code pertaining to the error detected
     */
    private static Integer checkMarkCommand(String command, TaskList arr, Integer errorCode) {
        try {
            String taskIndex = command.substring(5); //number
            if (checkCannotIntParse(taskIndex)) {
                errorCode = NO_FOLLOWING_INT;
                throw new ReimException(4, command);
            }
            int index = Integer.parseInt(taskIndex);
            if (index > arr.getSize() || index <= 0) {
                errorCode = INDEX_OUT_OF_BOUNDS;
                throw new ReimException(5, command);
            }
            Task t = arr.get(index - 1);
            if (t.getDone()) {
                errorCode = TASK_ALREADY_DONE; // task is already marked as done
                throw new ReimException(9, command);
            }
        } catch (ReimException e) {
            return errorCode;
        }
        return errorCode;
    }

    /**
     * Checking if a string can be converted to an integer
     *
     * @param s string to be tested
     * @return false if s can be converted to integer, else true
     */
    private static boolean checkCannotIntParse(String s) {
        try {
            Integer.parseInt(s);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    /**
     * checks for errors relating to the "unmark" command (cannot convert index string to integer,
     * index out of index of TaskList, Task already marked as not done)
     *
     * @param command command to be checked
     * @param arr the current tasklist
     * @param errorCode error code to be returned
     * @return error code pertaining to the error detected
     */
    private static Integer checkUnmarkCommand(String command, TaskList arr, Integer errorCode) {
        try {
            String taskIndex = command.substring(7); //number
            if (checkCannotIntParse(taskIndex)) {
                errorCode = NO_FOLLOWING_INT;
                throw new ReimException(NO_FOLLOWING_INT, command);
            }
            int index = Integer.parseInt(taskIndex);
            if (index > arr.getSize()) {
                errorCode = INDEX_OUT_OF_BOUNDS;
                throw new ReimException(INDEX_OUT_OF_BOUNDS, command);
            }
            Task t = arr.get(index - 1);
            if (!t.getDone()) {
                errorCode = TASK_ALREADY_NOT_DONE; // task is already marked as not done
                throw new ReimException(TASK_ALREADY_NOT_DONE, command);
            }
        } catch (ReimException e) {
            return errorCode;
        }
        return errorCode;
    }

    /**
     * checks for errors relating to the "todo" command (duplicate tasks)
     *
     * @param command command to be checked
     * @param arr the current tasklist
     * @return error code pertaining to the error detected
     */
    private static Integer checkToDoCommand(String command, TaskList arr) {
        try {
            if (checkForDuplicate(command, arr, 5, command.length() + 1)) {
                //duplicate task
                throw new ReimException(DUPLICATE_TASK, command);
            }
        } catch (ReimException e) {
            return DUPLICATE_TASK;
        }
        return NO_ERROR;
    }

    /**
     * checks for errors relating to the "deadline" command
     * (no timing given, no task given in command, duplicate tasks, timing does not follow correct format)
     *
     * @param command command to be checked
     * @param arr the current tasklist
     * @return error code pertaining to the error detected
     */
    private static Integer checkDeadlineCommand(String command, TaskList arr) {
        Integer startOfTaskIndex = 9;
        Integer offset = 4;
        try {
            if (checkForNoSecondPart(command, "/by")) { //no /by
                throw new ReimException(NO_TIMING_GIVEN, command); // invalid arguments: no timing given
            }
            int index = command.indexOf("/");
            if (checkForNoTaskPresent(command, startOfTaskIndex, index)) {
                throw new ReimException(NO_TASK_GIVEN, command); // invalid argument: no task given in command
            } else if (checkForNoTiming(command, index, offset)) {
                throw new ReimException(NO_TIMING_GIVEN, command);
            } else if (checkForDuplicate(command, arr, startOfTaskIndex, index)) {
                throw new ReimException(DUPLICATE_TASK, command); //duplicate task
            } else if (checkForWrongTimingFormat(command, index, offset)) {
                throw new ReimException(TIME_WRONG_FORMAT, command);
            } else if (checkCannotConvertToLocalDateTime(command, index, offset)) {
                throw new ReimException(CANNOT_CONVERT_TO_LOCALDATETIME, command);
            }
        } catch (ReimException e) {
            return e.getError();
        }
        return NO_ERROR;
    }

    private static boolean checkForNoSecondPart(String command, String checkCriteria) {
        try {
            assert command.contains(checkCriteria);
        } catch (AssertionError e) {
            return true;
        }
        return false;
    }

    private static boolean checkForNoTaskPresent(String command, Integer startIndex, Integer endIndex) {
        try {
            assert command.substring(startIndex, endIndex).isEmpty();
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    private static boolean checkForNoTiming(String command, Integer index, Integer offset) {
        try {
            assert command.substring(index + offset - 1).isEmpty();
        } catch (AssertionError e) {
            return false;
        }
        return true;
    }

    private static boolean checkForDuplicate(String command, TaskList tasks, Integer startIndex, Integer endIndex) {
        try {
            assert tasks.getArray().stream().anyMatch(x -> x.getTask()
                    .equals(command.substring(startIndex, endIndex - 1)));
        } catch (AssertionError e) {
            return false;
        }
        return true;
    }

    private static boolean checkForWrongTimingFormat(String command, Integer index, Integer offset) {
        try {
            boolean followsYearWithTimeFormat = command.substring(index + offset)
                    .matches("\\d{4}-\\d{2}-\\d{2} \\d{4}");
            boolean followsYearOnlyFormat = command.substring(index + offset).matches("\\d{4}-\\d{2}-\\d{2}");
            assert followsYearWithTimeFormat || followsYearOnlyFormat;
        } catch (AssertionError e) {
            return true;
        }
        return false;
    }

    private static boolean checkCannotConvertToLocalDateTime(String command, Integer index, Integer offset) {
        String deadline = command.substring(index + offset);
        if (deadline.length() == 15) { // yyyy-mm-dd tttt
            String dateString = deadline.substring(0, 10);
            String timingString = deadline.substring(11);
            String formattedTiming = new StringBuilder(timingString).insert(2, ":").toString();
            try {
                assert canStringConvertToLocalDate(dateString);
                assert canStringConvertToLocalTime(formattedTiming);
            } catch (AssertionError e) {
                return true;
            }
        } else {
            try {
                assert canStringConvertToLocalDate(deadline);
            } catch (AssertionError e) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks for errors relating to the "event" command (no timing given, no task given in command,
     * duplicate tasks, timing does not follow correct format)
     *
     * @param command command to be checked
     * @param arr the current tasklist
     * @return error code pertaining to the error detected
     */
    private static Integer checkEventCommand(String command, TaskList arr) {
        Integer startOfTaskIndex = 6;
        Integer offset = 6;
        try {
            if (checkForNoSecondPart(command, "/from")) {
                throw new ReimException(NO_TIMING_GIVEN, command); // invalid arguments: no timing given
            }
            int index = command.indexOf("/");
            if (checkForNoTaskPresent(command, startOfTaskIndex, index)) {
                throw new ReimException(NO_TASK_GIVEN, command); // invalid argument: no task given in command
            } else if (checkForNoTiming(command, index, offset)) {
                throw new ReimException(NO_TIMING_GIVEN, command);
            } else if (checkForDuplicate(command, arr, startOfTaskIndex, index)) {
                throw new ReimException(DUPLICATE_TASK, command); // duplicate task
            } else if (checkForWrongTimingFormat(command, index, offset)) {
                throw new ReimException(TIME_WRONG_FORMAT, command);
            } else if (checkCannotConvertToLocalDateTime(command, index, offset)) {
                throw new ReimException(CANNOT_CONVERT_TO_LOCALDATETIME, command);
            }
        } catch (ReimException e) {
            return e.getError();
        }
        return NO_ERROR;
    }

    /**
     * checks for errors relating to the "delete" command
     * (index string cannot convert to integer, index is out of index of TaskList)
     *
     * @param command command to be checked
     * @param arr the current tasklist
     * @param errorCode error code to be returned
     * @return error code pertaining to the error detected
     */
    private static Integer checkDeleteCommand(String command, TaskList arr, Integer errorCode) {
        try {
            String taskIndex = command.substring(7); //number
            if (checkCannotIntParse(taskIndex)) {
                throw new ReimException(NO_FOLLOWING_INT, command);
            }
            int index = Integer.parseInt(taskIndex);
            if (index > arr.getSize() || index <= 0) {
                throw new ReimException(INDEX_OUT_OF_BOUNDS, command);
            }
        } catch (ReimException e) {
            return e.getError(); // invalid command: mark command followed by char when it was meant to be an int
        }
        return errorCode;
    }
}
