import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    protected String fp;

    public Storage(String filePath) {
        this.fp = filePath;
    }

    public static ArrayList<Task> readFile(String filePath) {
        File f = new File(filePath);
        ArrayList<Task> output = new ArrayList<>();
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
                return new Todo(rest, "[X]");
            }
            return new Todo("[ ]", rest);
        }
        else if (type.equals("D")) {
            String[] p = rest.split(" \\| ");
            String task = p[0];
            String time = p[1];
            if (done.equals("1")) {
                return new Deadline("[X]", task, time);
            }
            return new Deadline("[ ]", task, time);
        }
        // its E
        String[] p = rest.split(" \\| ");
        String task = p[0];
        String time = p[1];
        if (done.equals("1")) {
            return new Event("[X]", task, time);
        }
        return new Event("[X]", task, time);
    }

    public static void saveArray(ArrayList<Task> arr, String dirPath, String filePath){
        File d = new File(dirPath);
        File f = new File(d, "Reim.txt");
        if (!d.exists()) {
            d.mkdirs();
        }
        try {
            FileWriter writer = new FileWriter(filePath, false);
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

        }
    }
}
