package Reim;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;
import java.util.ArrayList;
/**
 * Reim is the driver class of the entire application
 * @author Ruinim
 */
public class Reim {
    /**
     * storage is where our class methods of reading intial file and saving entries are located
     * items is the TaskList of items where our entries are
     * ui is the printing class
     */
    private Storage storage;
    private TaskList items;
    private Ui ui;

    public Reim(String dirPath, String filePath) {
        ui = new Ui();
        storage = new Storage(dirPath, filePath);
        items = new TaskList(storage.readFile());

    }

    /**
     * driver method
     */
    public void run() {
        ui.start();
        Scanner read = new Scanner(System.in);
        while (read.hasNext()) {
            String command = read.nextLine();
            if (command.equals("bye")) {
                ui.end();
                break;
            }
            Parser parser = new Parser(command, items);
//            Integer error = errorInCommand(command, items);
            Integer error = parser.errorInCommand();
            if (error > 0) {
                ui.printError(new ReimException(error, command));
                continue;
            }
            String output = parser.action();
            if (output.isEmpty()){
                String addition = parser.addingList();
                output = "Got it. I've added this task:\n" + addition
                        + "\nNow you have " + items.size() + " task(s) in the list.";
                // save list into ./data/Reim.Reim.txt

            }

//            saveArray(items, dirPath, filePath);
            storage.saveArray(items);
            ui.printOutput(output);
        }
    }

    public static void main(String[] args) {
        new Reim("src/data", "src/data/Reim.Reim.txt").run();
//        Reim.Ui ui = new Reim.Ui();
//        ui.start();
//        String filePath = "src/data/Reim.Reim.txt";
//        String dirPath = "src/data";
//        // read data from ./data/Reim.Reim.txt -> array data
//        // read line, split using | as delimiter
//        // check first T. D or E
//        // then call adding list
//        Reim.Storage storage = new Reim.Storage(dirPath, filePath);
////        ArrayList<Reim.Task> items = readFile(filePath);
//        Reim.TaskList items = storage.readFile();
//        Scanner read = new Scanner(System.in);
//        while (read.hasNext()) {
//            String command = read.nextLine();
//            if (command.equals("bye")) {
//                ui.end();
//                break;
//            }
//            Reim.Reim.Parser parser = new Reim.Reim.Parser(command, items);
////            Integer error = errorInCommand(command, items);
//            Integer error = parser.errorInCommand();
//            if (error > 0) {
//                ui.printError(new Reim.ReimException(error, command));
//                continue;
//            }
//            String output = parser.action();
//            if (output.isEmpty()){
//                String addition = parser.addingList();
//                output = "Got it. I've added this task:\n" + addition
//                        + "\nNow you have " + items.size() + " task(s) in the list.";
//                // save list into ./data/Reim.Reim.txt
//
//            }
//
////            saveArray(items, dirPath, filePath);
//            storage.saveArray(items);
//            ui.printOutput(output);
//        }
    }

    public static String addingList(String command, ArrayList<Task> arr) {
        if (command.startsWith("todo")) {
            arr.add(new Todo("[ ]", command.substring(5)));
            return new Todo("[ ]", command.substring(5)).toString();
        }
        else if (command.startsWith("deadline")) {
            int index = command.indexOf("/");
            String task = command.substring(9, index);
            String deadline = command.substring(index+ 4);
            if (deadline.length() == 15) { // yyyy-mm-dd tttt
                LocalDate date = LocalDate.parse(deadline.substring(0, 10));
                String timing = deadline.substring(11);
                String formatted_timing = new StringBuilder(timing).insert(2, ":").toString();
                LocalTime time = LocalTime.parse(formatted_timing);
                arr.add(new Deadline("[ ]", task, date, time));
                return new Deadline("[ ]", task, date, time).toString();
            }
            else {
                LocalDate date = LocalDate.parse(deadline);
                arr.add(new Deadline("[ ]", task, date));
                return new Deadline("[ ]", task, date).toString();
            }
        }
        else if (command.startsWith("event")) {
            int index = command.indexOf("/");
            String task = command.substring(6, index);
            String at = command.substring(index + 6);
            if (at.length() == 15) { // yyyy-mm-dd tttt
                LocalDate date = LocalDate.parse(at.substring(0, 10));
                String timing = at.substring(11);
                String formatted_timing = new StringBuilder(timing).insert(2, ":").toString();
                LocalTime time = LocalTime.parse(formatted_timing);
                arr.add(new Event("[ ]", task, date, time));
                return new Event("[ ]", task, date, time).toString();
            }
            LocalDate date = LocalDate.parse(at);
            arr.add(new Event("[ ]", task, date));
            return new Event("[ ]", task, date).toString();
        }
        return "";
    }

    public static String message(String msg) {
        return "____________________________________________________________\n"
                + msg + "\n"
                + "____________________________________________________________\n";
    }

    public static Integer commandParse(String command) {
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

    public static String action(String command, ArrayList<Task> arr) {
        Integer commandType = commandParse(command);
        String finalOutput = "";
        if (commandType.equals(2)) { //list
            finalOutput = message(listOutput(arr));
        }
        else if (commandType.equals(3)) { //mark
            String taskIndex = command.substring(5); //number
            Task t = arr.get(Integer.parseInt(taskIndex) - 1);
            arr.set(Integer.parseInt(taskIndex) - 1, t.mark());
            finalOutput = message("Nice! I've marked this task as done:\n" + t.mark());
        }
        else if (commandType.equals(4)) { //unmark
            String taskIndex = command.substring(7); //number
            Task t = arr.get(Integer.parseInt(taskIndex) - 1);
            arr.set(Integer.parseInt(taskIndex) - 1, t.unmark());
            finalOutput = message("OK, I've marked this task as not done yet:\n" + t.unmark());
        }
        else if (commandType.equals(5)) { //delete
            String taskIndex = command.substring(7);
            Task t = arr.get(Integer.parseInt(taskIndex) - 1);
            arr.remove(Integer.parseInt(taskIndex) - 1);
            finalOutput = message("Noted, I've removed this task:\n" + t +"\nNow you have "
                    + arr.size() + " task(s) in the list");
        }
        return finalOutput;
    }

    public static String listOutput(ArrayList<Task> arr) {
        StringBuilder finalString = new StringBuilder();
        for (int i = 1; i - 1 < arr.size(); i++) {
            finalString.append(i).append(". ").append(arr.get(i - 1).toString()).append("\n");
        }
        return finalString.toString();
    }

    public static Integer errorInCommand(String command, ArrayList<Task> arr) {
        // list, todo, event, deadline, mark, unmark
        String[] command_list = {"list", "todo", "deadline", "event", "mark", "unmark", "delete"};
        int error_code = 0;
        int count = 0;
        for (int i = 0; i < command_list.length; i++) {
            if (command.startsWith(command_list[i])) {
                count++;
                if (!command.equals("list") && command.length() < command_list[i].length() + 2) {
                    return 2; // missing arguments
                }
                else if (command.startsWith("list") && command.length() > 4) {
                    return 3; //invalid command: list command does not have arguments
                }
                else if (command.startsWith("mark")) {
                    error_code = markCheck(command, arr, error_code);
                }
                else if (command.startsWith("unmark")) {
                    error_code = unmarkCheck(command, arr, error_code);
                }
                else if (command.startsWith("delete")) {
                    error_code = deleteCheck(command, arr, error_code);
                }
                else if (command.startsWith("todo")) {
                    error_code = todoCheck(command, arr);
                }
                else if (command.startsWith("deadline")) {
                    error_code = deadlineCheck(command, arr);
                }
                else if (command.startsWith("event")) {
                    error_code = eventCheck(command, arr);
                }

            }
        }
        if (count < 1) {
            return 1; //invalid command: please use the commands list, todo event, deadline, mark, unmark
        }
        return error_code;
    }

    public static Integer markCheck(String command, ArrayList<Task> arr, Integer error_code){
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
                error_code = 9; // task is already marked as not done
                throw new ReimException(9, command);
            }
        } catch (ReimException e) {
            return error_code;
        }
        return error_code;
    }

    public static boolean cannotIntParse(String s) {
        try {
            Integer.parseInt(s);
            return false;
        }
        catch (NumberFormatException e) {
            return true;
        }
    }

    public static Integer unmarkCheck(String command, ArrayList<Task> arr, Integer error_code) {
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

    public static Integer todoCheck(String command, ArrayList<Task> arr) {
        try {
            if (arr.stream().anyMatch(x -> x.getTask().equals(command.substring(5)))) {
                //duplicate task
                throw new ReimException(10, command);
            }
        }
        catch (ReimException e) {
            return 10;
        }
        return 0;
    }

    public static Integer deadlineCheck(String command, ArrayList<Task> arr) {
        try {
            if (!command.contains("/by")) { //no /by
                throw new ReimException(6, command); // invalid arguments: no timing given
            }
            int index = command.indexOf("/");
            if (command.substring(9, index).isEmpty()) {
                throw new ReimException(7, command); // invalid argument: no task given in command
            } else if (command.substring(index + 3).isEmpty()) {
                throw new ReimException(6, command);
            } else if (arr.stream().anyMatch(x -> x.getTask().equals(command.substring(9, index)))) {
                throw new ReimException(10, command); //duplicate task
            }
            else if (!(command.substring(index + 4).matches("\\d{4}-\\d{2}-\\d{2} \\d{4}") || command.substring(index + 4).matches("\\d{4}-\\d{2}-\\d{2}"))) {
                throw new ReimException(11, command);
            }
        }
        catch (ReimException e) {
            return e.getError();
        }
        return 0;

    }

    public static Integer eventCheck(String command, ArrayList<Task> arr) {
        try {
            if (!command.contains("/from")) {
                throw new ReimException(6, command); // invalid arguments: no timing given
            }
            int index = command.indexOf("/");
            if (command.substring(6, index).isEmpty()) {
                throw new ReimException(7, command); // invalid argument: no task given in command
            } else if (command.substring(index + 5).isEmpty()) {
                throw new ReimException(6, command);
            } else if (arr.stream().anyMatch(x -> x.getTask().equals(command.substring(6, index)))) {
                throw new ReimException(10, command); // duplicate task
            }
            else if (!(command.substring(index + 6).matches("\\d{4}-\\d{2}-\\d{2} \\d{4}") || command.substring(index + 6).matches("\\d{4}-\\d{2}-\\d{2}"))) {
                throw new ReimException(11, command);
            }
        }
        catch (ReimException e) {
            return e.getError();
        }
        return 0;
    }

    public static Integer deleteCheck(String command, ArrayList<Task> arr, Integer error_code) {
        try {
            String taskIndex = command.substring(7); //number
            if (cannotIntParse(taskIndex)) {
                throw new ReimException(4, command);
            }
            int index = Integer.parseInt(taskIndex);
            if (index > arr.size()) {
                throw new ReimException(5, command);
            }
            Task t = arr.get(Integer.parseInt(taskIndex) - 1);
        } catch (ReimException e) {
            return e.getError(); // invalid command: mark command followed by char when it was meant to be an int
        }
        return error_code;
    }

//    public static ArrayList<Reim.Task> readFile(String filePath) {
//        File f = new File(filePath);
//        ArrayList<Reim.Task> output = new ArrayList<>();
//        if (!f.exists()) {
//            return output;
//        }
//        try {
//            Scanner reader = new Scanner(f);
//            while (reader.hasNextLine()) {
//                String data = reader.nextLine();
//                output.add(parser(data));
//            }
//        } catch (FileNotFoundException ignored) {
//
//        }
//        return output;
//    }
////
//    public static Reim.Task parser(String command) {
//        String type = String.valueOf(command.charAt(0));
//        String done = String.valueOf(command.charAt(4));
//        String rest = command.substring(8);
//
//        if (type.equals("T")) {
//            if (done.equals("1")) {
//                return new Reim.Todo(rest, "[X]");
//            }
//            return new Reim.Todo("[ ]", rest);
//        }
//        else if (type.equals("D")) {
//            String[] p = rest.split(" \\| ");
//            String task = p[0];
//            String time = p[1];
//            String[] dt = time.split(" ");
//            if (dt.length == 2) {
//                LocalDate date = LocalDate.parse(dt[0]);
//                String timing = dt[1];
//                LocalTime lt = LocalTime.parse(timing);
//                if (done.equals("1")) {
//                    return new Reim.Reim.Deadline("[X]", task, date, lt);
//                }
//                return new Reim.Reim.Deadline("[ ]", task, date, lt);
//            }
//            if (done.equals("1")) {
//                return new Reim.Reim.Deadline("[X]", task, time);
//            }
//            return new Reim.Reim.Deadline("[ ]", task, time);
//        }
//        // its E
//        String[] p = rest.split(" \\| ");
//        String task = p[0];
//        String time = p[1];
//        String[] dt = time.split(" ");
//        if (dt.length == 2) {
//            LocalDate date = LocalDate.parse(dt[0]);
//            String timing = dt[1];
//            LocalTime lt = LocalTime.parse(timing);
//            if (done.equals("1")) {
//                return new Reim.Reim.Event("[X]", task, date, lt);
//            }
//            return new Reim.Reim.Event("[ ]", task, date, lt);
//        }
//        if (done.equals("1")) {
//            return new Reim.Reim.Event("[X]", task, time);
//        }
//        return new Reim.Reim.Event("[X]", task, time);
//    }
//

//    public static void saveArray(ArrayList<Reim.Task> arr, String dirPath, String filePath){
//        File d = new File(dirPath);
//        File f = new File(d, "Reim.Reim.txt");
//        if (!d.exists()) {
//            d.mkdirs();
//        }
//        try {
//            FileWriter writer = new FileWriter(filePath, false);
//            StringBuilder output = new StringBuilder();
//            for (int i = 0; i < arr.size(); i++) {
//                output.append(arr.get(i).formattedString());
//                output.append("\n");
//            }
//            String finalOutput = output.toString();
//            writer.write(finalOutput);
//            writer.close();
//        }
//        catch (IOException ignored) {
//
//        }
//    }


}
