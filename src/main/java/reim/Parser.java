package reim;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * The Parser class handles the interpretation and validation of user commands
 * in the Reim application. It verifies if commands are valid and determines the appropriate
 * action to be taken on the task list based on those commands.
 * <p>
 * supported commands: todo, deadline, event, unmark, mark, delete, find, update, list
 * <p>
 * Errors are handled via the ReimException class, with specific error codes.
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

    static final Integer ADD = 51;
    static final Integer LIST = 52;
    static final Integer MARK = 53;
    static final Integer UNMARK = 54;
    static final Integer DELETE = 55;
    static final Integer FIND = 56;
    static final Integer UPDATE = 57;

    /**
     * The command string to be parsed and acted upon.
     */
    private final String command;
    /**
     * The list of tasks that the parser will operate on.
     */
    private TaskList tasks;

    /**
     * Constructs a Parser instance to handle a specific command and task list.
     *
     * @param command String of command to parse
     * @param tasks the current list of tasks to operate on
     */
    public Parser(String command, TaskList tasks) {
        this.command = command;
        this.tasks = tasks;
    }

    /**
     * Parses and adds a new task (todo, deadline, or event) to the task list based on the command.
     *
     * @return string representation of the new task to be added to the tasklist
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

    /**
     * Method to add the new todo event into the tasklist
     *
     * @param command the command that contains the task to be added (e.g., "todo read book")
     * @param tasks our current tasklist
     * @return string representation of the todo object that was added
     */
    private static String addToDoToList(String command, TaskList tasks) {
        tasks.add(new Todo(false, command.substring(5)));
        return new Todo(false, command.substring(5)).toString();
    }

    /**
     * Method to add the new deadline task into the tasklist
     *
     * @param command the command that contains the task details (eg. "deadline assignment /by 2025-09-19 2359")
     * @param tasks our current tasklist
     * @return string representation of the deadline object that was added
     */
    private static String addDeadlineToList(String command, TaskList tasks) {
        int index = command.indexOf("/");
        String task = command.substring(9, index - 1);
        String deadline = command.substring(index + 4);
        if (deadline.length() == 15) {
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

    /**
     * Method to add the new event task into the tasklist
     *
     * @param command the command that contains the task details (eg. "event party /from 2025-09-20 0000")
     * @param tasks our current tasklist
     * @return string representation of the event object that was added
     */
    private static String addEventToList(String command, TaskList tasks) {
        int index = command.indexOf("/");
        String task = command.substring(6, index - 1);
        String at = command.substring(index + 6);
        if (at.length() == 15) {
            String dateString = at.substring(0, 10);
            String timingString = at.substring(11);
            String formattedTiming = new StringBuilder(timingString).insert(2, ":").toString();

            LocalDate date = LocalDate.parse(dateString);
            LocalTime time = LocalTime.parse(formattedTiming);
            tasks.add(new Event(false, task, date, time));
            return new Event(false, task, date, time).toString();
        } else {
            LocalDate date = LocalDate.parse(at);
            tasks.add(new Event(false, task, date));
            return new Event(false, task, date).toString();
        }
    }

    /**
     * check to see if the string given can be converted to LocalDate object without error
     *
     * @param dateString the string to be checked
     * @return true if string can be converted, else false
     */
    private static boolean canStringConvertToLocalDate(String dateString) {
        try {
            LocalDate time = LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Check to see if the string given can be converted to LocalTime object without error
     *
     * @param timeString the string to be checked
     * @return true if string can be converted, else false
     */
    private static boolean canStringConvertToLocalTime(String timeString) {
        try {
            LocalTime time = LocalTime.parse(timeString);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Executes the given command that does not involve adding a new task
     * (e.g., list, mark, unmark, delete, find, update).
     *
     * @return the output message generated by executing the command
     */
    public String action() {
        Integer commandType = commandParse(this.command);
        String finalOutput = "";
        assert commandType > ADD - 1 && commandType < UPDATE + 1;
        if (commandType.equals(LIST)) { //list
            finalOutput = showListOutput(this.tasks);
        } else if (commandType.equals(MARK)) { //mark
            finalOutput = mark(this.command, this.tasks);
        } else if (commandType.equals(UNMARK)) { //unmark
            finalOutput = unmark(this.command, this.tasks);
        } else if (commandType.equals(DELETE)) { //delete
            finalOutput = delete(this.command, this.tasks);
        } else if (commandType.equals(FIND)) {
            finalOutput = find(this.command, this.tasks);
        } else if (commandType.equals(UPDATE)) {
            finalOutput = updateList(this.command, this.tasks);

        }
        return finalOutput;
    }

    /**
     * Marks the specified task as completed.
     *
     * @param command the command string (e.g., "mark 2")
     * @param tasks the task list to update
     * @return a confirmation message with the updated task
     */
    private static String mark(String command, TaskList tasks) {
        String taskIndex = command.substring(5);
        Task t = tasks.get(Integer.parseInt(taskIndex) - 1);
        tasks.set(Integer.parseInt(taskIndex) - 1, t.mark());
        return "Nice! I've marked this task as done:\n" + t.mark();
    }

    /**
     * this is the actions of the unmark command
     *
     * @param command command to be executed
     * @param tasks our tasklists to act upon
     * @return String reply output of command
     */
    private static String unmark(String command, TaskList tasks) {
        String taskIndex = command.substring(7); //number
        Task t = tasks.get(Integer.parseInt(taskIndex) - 1);
        tasks.set(Integer.parseInt(taskIndex) - 1, t.unmark());
        return "OK, I've marked this task as not done yet:\n" + t.unmark();
    }

    /**
     * this is the actions of the delete command
     *
     * @param command command to be executed
     * @param tasks our tasklist to act upon
     * @return String reply output of command
     */
    private static String delete(String command, TaskList tasks) {
        String taskIndex = command.substring(7);
        Task t = tasks.get(Integer.parseInt(taskIndex) - 1);
        tasks.remove(Integer.parseInt(taskIndex) - 1);
        return "Noted, I've removed this task:\n" + t + "\nNow you have "
                + tasks.getSize() + " task(s) in the list";
    }

    /**
     * this is the actions of the find command
     *
     * @param command command to be executed
     * @param tasks our tasklist to act upon
     * @return String reply output of command
     */
    private static String find(String command, TaskList tasks) {
        String searchCriteria = command.substring(5);
        TaskList t = tasks.searchList(searchCriteria);
        if (t.getSize() == 0) {
            return "There were no matches in your list\n";
        } else {
            return "Here are the matching tasks in your list:\n" + showListOutput(t);
        }
    }

    /**
     * this is the actions of the update command
     *
     * @param command command to be executed
     * @param tasks our tasklist to act upon
     * @return String reply output of command
     */
    private static String updateList(String command, TaskList tasks) {
        String remainingString = command.substring(7);
        String[] indexAndCommand = remainingString.split(" ", 2);
        Integer index = Integer.parseInt(indexAndCommand[0]);
        String newCommand = indexAndCommand[1];

        Task t = tasks.get(index - 1);
        boolean isTDone = t.getDone();
        assert commandParse(newCommand).equals(ADD);
        Parser p = new Parser(newCommand, tasks);
        Integer pErrorCode = p.errorInCommand();
        if (pErrorCode != 0 && !pErrorCode.equals(DUPLICATE_TASK)) {
            return "Inner command error";
        }
        if (newCommand.startsWith("todo")) {
            setTodo(newCommand, tasks, index, isTDone);
        } else if (newCommand.startsWith("deadline")) {
            setDeadline(newCommand, tasks, index, isTDone);
        } else if (newCommand.startsWith("event")) {
            setEvent(newCommand, tasks, index, isTDone);
        }
        return "I have edited the task at index: " + index + " to " + tasks.get(index - 1);
    }

    /**
     * Setting method to edit the tasklist with a new todo task
     *
     * @param newCommand command to be executed
     * @param tasks current tasklist to act upon
     * @param index index of task to be updated
     * @param isTDone the done status of the previous task (we want to maintain the status)
     */
    private static void setTodo(String newCommand, TaskList tasks, Integer index, boolean isTDone) {
        tasks.set(index - 1, new Todo(isTDone, newCommand.substring(5)));
    }

    /**
     * Setting method to edit the tasklist with a new deadline task
     *
     * @param command command to be executed
     * @param tasks current tasklist to act upon
     * @param index index of task to be updated
     * @param isTDone the done status of the previous task (we want to maintain the status)
     */
    private static void setDeadline(String command, TaskList tasks, Integer index, boolean isTDone) {
        int indexOfDeadline = command.indexOf("/");
        String task = command.substring(9, indexOfDeadline - 1);
        String deadline = command.substring(indexOfDeadline + 4);
        if (deadline.length() == 15) {
            String dateString = deadline.substring(0, 10);
            String timingString = deadline.substring(11);
            String formattedTiming = new StringBuilder(timingString).insert(2, ":").toString();

            LocalDate date = LocalDate.parse(dateString);
            LocalTime time = LocalTime.parse(formattedTiming);
            tasks.set(index - 1, new Deadline(isTDone, task, date, time));
        } else {
            LocalDate date = LocalDate.parse(deadline);
            tasks.set(index - 1, new Deadline(isTDone, task, date));
        }
    }

    /**
     * Setting method to edit the tasklist with a new event task
     *
     * @param command command to be executed
     * @param tasks current tasklist to act upon
     * @param index index of task to be updated
     * @param isTDone the done status of the previous task (we want to maintain the status)
     */
    private static void setEvent(String command, TaskList tasks, Integer index, boolean isTDone) {
        int indexOfEvent = command.indexOf("/");
        String task = command.substring(6, indexOfEvent - 1);
        String at = command.substring(indexOfEvent + 6);
        if (at.length() == 15) { // yyyy-mm-dd tttt
            String dateString = at.substring(0, 10);
            String timingString = at.substring(11);
            String formattedTiming = new StringBuilder(timingString).insert(2, ":").toString();

            LocalDate date = LocalDate.parse(dateString);
            LocalTime time = LocalTime.parse(formattedTiming);
            tasks.set(index - 1, new Event(isTDone, task, date, time));
        } else {
            LocalDate date = LocalDate.parse(at);
            tasks.set(index - 1, new Event(isTDone, task, date));
        }
    }

    /**
     * Checks which command is to be called (list, mark, unmark, delete) and returns the correct integer
     *
     * @param command is the command to be checked
     * @return An Integer respective to the different possible commands
     */
    private static Integer commandParse(String command) {
        if (command.equals("list")) {
            return LIST;
        } else if (command.startsWith("mark")) {
            return MARK;
        } else if (command.startsWith("unmark")) {
            return UNMARK;
        } else if (command.startsWith("delete")) {
            return DELETE;
        } else if (command.startsWith("find")) {
            return FIND;
        } else if (command.startsWith("update")) {
            return UPDATE;
        }
        return ADD;
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
     * Validates the syntax and semantics of the command string.
     * Returns an appropriate error code based on the issue encountered.
     *
     * @return error code indicating the type of issue found (0 if no error)
     */
    public Integer errorInCommand() {
        // list, todo, event, deadline, mark, unmark
        String[] commandList = {"list", "todo", "deadline", "event", "mark", "unmark", "delete", "find", "update"};
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
            // invalid command: please use the commands list, todo event, deadline, mark, unmark
            return NO_COMMAND;
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
            String taskIndex = command.substring(5);
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
                errorCode = TASK_ALREADY_DONE;
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
            String taskIndex = command.substring(7);
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
                errorCode = TASK_ALREADY_NOT_DONE;
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
            if (checkForNoSecondPart(command, "/by")) {
                throw new ReimException(NO_TIMING_GIVEN, command);
            }
            int index = command.indexOf("/");
            if (checkForNoTaskPresent(command, startOfTaskIndex, index)) {
                throw new ReimException(NO_TASK_GIVEN, command);
            } else if (checkForNoTiming(command, index, offset)) {
                throw new ReimException(NO_TIMING_GIVEN, command);
            } else if (checkForDuplicate(command, arr, startOfTaskIndex, index)) {
                throw new ReimException(DUPLICATE_TASK, command);
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
     * check if the command includes the delimiter used to separate different parts of the command
     *
     * @param command the command string to be checked for
     * @param checkCriteria the delimiter to check if there is anything behind ( "/by" and "/from")
     * @return true if delimiter is absent, else false
     */
    private static boolean checkForNoSecondPart(String command, String checkCriteria) {
        try {
            assert command.contains(checkCriteria);
        } catch (AssertionError e) {
            return true;
        }
        return false;
    }

    /**
     * check if the command has a task
     *
     * @param command command string to be checked
     * @param startIndex the start of the task string
     * @param endIndex end of the task string
     * @return true if there is no task string to be found, else false
     */
    private static boolean checkForNoTaskPresent(String command, Integer startIndex, Integer endIndex) {
        try {
            assert command.substring(startIndex, endIndex).isEmpty();
        } catch (AssertionError e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Checks if the date/time argument is missing in the command string.
     *
     * @param command the command to check
     * @param index the index where the date/time delimiter starts (e.g., "/by", "/from")
     * @param offset the number of characters to skip past the delimiter
     * @return true if the timing argument is missing, false otherwise
     */
    private static boolean checkForNoTiming(String command, Integer index, Integer offset) {
        try {
            assert command.substring(index + offset - 1).isEmpty();
        } catch (AssertionError e) {
            return false;
        }
        return true;
    }

    /**
     * check if the task being added is already in the list
     *
     * @param command command string to be checked
     * @param tasks our current tasklist
     * @param startIndex starting index of the task
     * @param endIndex ending index of the task
     * @return true if task can be found already in the tasklist, else false
     */
    private static boolean checkForDuplicate(String command, TaskList tasks, Integer startIndex, Integer endIndex) {
        try {
            assert tasks.getArray().stream().anyMatch(x -> x.getTask()
                    .equals(command.substring(startIndex, endIndex - 1)));
        } catch (AssertionError e) {
            return false;
        }
        return true;
    }

    /**
     * check if the timing given is of the wrong format
     *
     * @param command command string to be checked
     * @param index index of /from or /by
     * @param offset the length of /from or /by to push index to the date/time
     * @return true if date/time given does not follow the required format, else false
     */
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

    /**
     * Checks whether the date/time substring in the command can be parsed into LocalDate and/or LocalTime.
     *
     * @param command the command containing the date/time
     * @param index the index of the date/time delimiter ("/by" or "/from")
     * @param offset the offset to start reading the date/time value
     * @return true if the date/time string is invalid or unparseable, false otherwise
     */
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
                throw new ReimException(NO_TIMING_GIVEN, command);
            }
            int index = command.indexOf("/");
            if (checkForNoTaskPresent(command, startOfTaskIndex, index)) {
                throw new ReimException(NO_TASK_GIVEN, command);
            } else if (checkForNoTiming(command, index, offset)) {
                throw new ReimException(NO_TIMING_GIVEN, command);
            } else if (checkForDuplicate(command, arr, startOfTaskIndex, index)) {
                throw new ReimException(DUPLICATE_TASK, command);
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
            String taskIndex = command.substring(7);
            if (checkCannotIntParse(taskIndex)) {
                throw new ReimException(NO_FOLLOWING_INT, command);
            }
            int index = Integer.parseInt(taskIndex);
            if (index > arr.getSize() || index <= 0) {
                throw new ReimException(INDEX_OUT_OF_BOUNDS, command);
            }
        } catch (ReimException e) {
            return e.getError();
        }
        return errorCode;
    }
}
