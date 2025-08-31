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
    /**
     * dp is the directory path given
     * fp is the file path given
     */
    private final String dp;
    private final String fp;

    /**
     * Constructor method for storage
     *
     * @param dirPath is the relative directory path of where the file wanted is located
     * @param filePath is the relative file path of where the file wanted is located
     */
    public Storage(String dirPath, String filePath) {
        this.dp = dirPath;
        this.fp = filePath;
    }

    /**
     * Reads the files from the file path given during the creation of the object
     *
     * @return TaskList generated from the file given
     */
    public TaskList readFile() throws ReimException {
        File f = new File(this.fp);
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
            throw new ReimException(12, "");
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
            if (done.equals("1")) {
                return new Todo("[X]", rest);
            }
            return new Todo("[ ]", rest);
        } else if (type.equals("D")) {
            String[] p = rest.split(" \\| ");
            String task = p[0];
            String time = p[1];
            String[] dt = time.split(" ");

            if (dt.length == 2) {
                LocalDate date = LocalDate.parse(dt[0]);
                String timing = dt[1];
                LocalTime lt = LocalTime.parse(timing);
                if (done.equals("1")) {
                    return new Deadline("[X]", task, date, lt);
                }
                return new Deadline("[ ]", task, date, lt);
            }
            if (done.equals("1")) {
                return new Deadline("[X]", task, time);
            }
            return new Deadline("[ ]", task, time);
        }
        // its E
        String[] p = rest.split(" \\| ");
        String task = p[0];
        String time = p[1];
        String[] dt = time.split(" ");

        if (dt.length == 2) {
            LocalDate date = LocalDate.parse(dt[0]);
            String timing = dt[1];
            LocalTime lt = LocalTime.parse(timing);
            if (done.equals("1")) {
                return new Event("[X]", task, date, lt);
            }
            return new Event("[ ]", task, date, lt);
        }
        if (done.equals("1")) {
            return new Event("[X]", task, time);
        }
        return new Event("[X]", task, time);
    }

    /**
     * Saving the entries of our current TaskList into the file
     *
     * @param arr taskList to be saved into the external file
     */
    public void saveArray(TaskList arr) {
        File d = new File(this.dp);

        if (!d.exists()) {
            d.mkdirs();
        }
        try {
            FileWriter writer = new FileWriter(this.fp, false);
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
        }
    }
}
