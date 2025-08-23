package Reim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Storage {
    protected String dp;
    protected String fp;

    public Storage(String dirPath, String filePath) {
        this.dp = dirPath;
        this.fp = filePath;
    }

    public TaskList readFile() {
        File f = new File(this.fp);
        TaskList output = new TaskList();
        if (!f.exists()) {
            return output;
        }
        try {
            Scanner reader = new Scanner(f);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                output.add(parser(data));
            }
        } catch (FileNotFoundException ignored) {

        }
        return output;
    }

    public static Task parser(String command) {
        String type = String.valueOf(command.charAt(0));
        String done = String.valueOf(command.charAt(4));
        String rest = command.substring(8);

        if (type.equals("T")) {
            if (done.equals("1")) {
                return new Todo("[X]", rest);
            }
            return new Todo("[ ]", rest);
        }
        else if (type.equals("D")) {
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

    public void saveArray(TaskList arr){
        File d = new File(this.dp);
        File f = new File(d, "Reim.Reim.txt");
        if (!d.exists()) {
            d.mkdirs();
        }
        try {
            FileWriter writer = new FileWriter(this.fp, false);
            StringBuilder output = new StringBuilder();
            for (int i = 0; i < arr.size(); i++) {
                output.append(arr.get(i).formattedString());
                output.append("\n");
            }
            String finalOutput = output.toString();
            writer.write(finalOutput);
            writer.close();
        }
        catch (IOException ignored) {
            // refers to if there is nothing to read from file
        }
    }
}
