package reim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * Handles loading from and saving to the local data file for the application.
 * <p>
 *     Reading saved tasks from a file and converting them into Task objects
 *     Writing the current list of tasks to disk for persistence between sessions
 * </p>
 * @author Ruinim
 */
public class Storage {
    static final Integer NO_FILE_FOUND = 12;
    static final Integer WRITE_FAILED = 14;

    /** Directory path where the save file resides.*/
    private final String directoryPath;
    /** File path of the save file. */
    private final String filePath;

    /**
     * Constructor method for storage
     *
     * @param dirPath is the relative directory path of where the file wanted is located
     * @param filePath is the relative file path of where the file wanted is located
     */
    public Storage(String dirPath, String filePath) {
        this.directoryPath = dirPath;
        this.filePath = filePath;
    }

    /**
     * Reads saved tasks from the file path and reconstructs the TaskList.
     * <p>
     * If the file does not exist, returns an empty task list without error.
     * </p>
     *
     * @return A {@code TaskList} populated from the file, or empty if file is missing.
     * @throws ReimException if the file is found but unreadable.
     */
    public TaskList readFile() throws ReimException {
        File f = new File(this.filePath);
        TaskList output = new TaskList();
        if (!f.exists()) {
            return output;
        }
        try {
            Scanner reader = new Scanner(f);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                output.add(parseLine(data));
            }
        } catch (FileNotFoundException ignored) {
            throw new ReimException(NO_FILE_FOUND, "");
        }
        return output;
    }

    /**
     * Parses a single line of file data and converts it to a Task.
     *
     * @param command A formatted line representing a task.
     * @return A corresponding Task object.
     */
    private static Task parseLine(String command) {
        String type = String.valueOf(command.charAt(0));
        String done = String.valueOf(command.charAt(4));
        String rest = command.substring(8);

        if (type.equals("T")) {
            return parseTodo(done, rest);
        } else if (type.equals("D")) {
            return parseDeadline(done, rest);
        }
        // its E
        return parseEvent(done, rest);
    }

    /**
     * generate the todo task to be added to the list
     *
     * @param done the done status of the task
     * @param task the descriptor of the task
     * @return the new todo object
     */
    private static Todo parseTodo(String done, String task) {
        if (done.equals("1")) {
            return new Todo(true, task);
        }
        return new Todo(false, task);
    }

    /**
     * generate the deadline task to be added to the list
     *
     * @param done the done status of the task
     * @param restOfCommand the descriptor of the task and the date/time
     * @return the new deadline object
     */
    private static Deadline parseDeadline(String done, String restOfCommand) {
        String[] p = restOfCommand.split(" \\| ");
        String task = p[0];
        String time = p[1];
        String[] dateTime = time.split(" ");

        if (dateTime.length == 2) {
            LocalDate date = LocalDate.parse(dateTime[0]);
            String timing = dateTime[1];
            LocalTime finalTiming = LocalTime.parse(timing);
            if (done.equals("1")) {
                return new Deadline(true, task, date, finalTiming);
            }
            return new Deadline(false, task, date, finalTiming);
        }
        if (done.equals("1")) {
            return new Deadline(true, task, time);
        }
        return new Deadline(false, task, time);
    }

    /**
     * generate the event task to be added to the list
     *
     * @param done the done status of the task
     * @param restOfCommand the descriptor of the task and the date/time
     * @return the new deadline object
     */
    private static Event parseEvent(String done, String restOfCommand) {
        String[] p = restOfCommand.split(" \\| ");
        String task = p[0];
        String time = p[1];
        String[] dateTime = time.split(" ");

        if (dateTime.length == 2) {
            LocalDate date = LocalDate.parse(dateTime[0]);
            String timing = dateTime[1];
            LocalTime finalTime = LocalTime.parse(timing);
            if (done.equals("1")) {
                return new Event(true, task, date, finalTime);
            }
            return new Event(false, task, date, finalTime);
        }
        if (done.equals("1")) {
            return new Event(true, task, time);
        }
        return new Event(false, task, time);
    }

    /**
     * Saves the current list of tasks to the specified file.
     * <p>
     * This method ensures the directory exists, and overwrites the file
     * with the current list of tasks in formatted string form.
     * </p>
     *
     * @param arr The TaskList to be saved.
     */
    public void saveArray(TaskList arr) {
        File d = new File(this.directoryPath);

        if (!d.exists()) {
            d.mkdirs();
        }
        try {
            FileWriter writer = new FileWriter(this.filePath, false);
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < arr.getSize(); i++) {
                output.append(arr.get(i).generateFormattedString());
                output.append("\n");
            }
            String finalOutput = output.toString();
            writer.write(finalOutput);
            writer.close();
        } catch (IOException ignored) {
            // refers to if there is nothing to read from file
            System.out.println(new ReimException(WRITE_FAILED, "").getErrorMessage());
        }
    }
}
