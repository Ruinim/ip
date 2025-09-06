package reim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

/**
 * Class at which we use to read the initial file and to write into a data file to save our entries
 * @author Ruinim
 */
public class Storage {
    static final Integer NO_FILE_FOUND = 12;
    static final Integer WRITE_FAILED = 14;
    /**
     * dp is the directory path given
     * fp is the file path given
     */
    private final String directoryPath;
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
     * Reads the files from the file path given during the creation of the object
     *
     * @return TaskList generated from the file given
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
     * processes the string commands in the file and coverts them to their respective tasks to be added into TaskList
     *
     * @param command the command to process
     * @return the Task generated form the string
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
     * Saving the entries of our current TaskList into the file
     *
     * @param arr taskList to be saved into the external file
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
