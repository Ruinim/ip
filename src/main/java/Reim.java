import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class Reim {
    public static void main(String[] args) {
        String logo = """
                ____________________________________________________________
                Hello! I'm Reim
                What can I do for you?
                ____________________________________________________________
                """;
        String end = """
                ____________________________________________________________
                Bye. Hope to see you again soon!
                ____________________________________________________________
                """;
        System.out.println(logo);
        String filePath = "src/data/Reim.txt";
        String dirPath = "src/data";
        // read data from ./data/Reim.txt -> array data
        // read line, split using | as delimiter
        // check first T. D or E
        // then call adding list
        ArrayList<Task> items = readFile(filePath);
        Scanner read = new Scanner(System.in);
        while (read.hasNext()) {
//            Scanner read = new Scanner(System.in);
            String command = read.nextLine();
            if (command.equals("bye")) {
                System.out.println(end);
                break;
            }
            Integer error = errorInCommand(command, items);
            if (error > 0) {
                System.out.println(new ReimException(error, command).errorMessage());
                continue;
            }
            String output = action(command, items);
//            System.out.println(output);
            if (output.isEmpty()){
                String addition = addingList(command, items);
                output = message("Got it. I've added this task:\n" + addition
                        + "\nNow you have " + items.size() + " task(s) in the list.");
                // save list into ./data/Reim.txt

            }

            saveArray(items, dirPath, filePath);
            System.out.println(output);
        }




    }

    public static String addingList(String command, ArrayList<Task> arr) {
        if (command.startsWith("todo")) {
            arr.add(new Todo("[ ]", command.substring(5)));
            return new Todo("[ ]", command.substring(5)).toString();
        }
        else if (command.startsWith("deadline")) {
            int index = command.indexOf("/");
            String task = command.substring(9, index);
            String deadline = command.substring(index+ 3);
            arr.add(new Deadline("[ ]", task, deadline));
            return new Deadline("[ ]", task, deadline).toString();
        }
        else if (command.startsWith("event")) {
            int index = command.indexOf("/");
            String task = command.substring(6, index);
            String at = command.substring(index + 5);
            arr.add(new Event("[ ]", task, at));
            return new Event("[ ]", task, at).toString();
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
//            System.out.println(finalOutput);
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
//        try {
//            String taskIndex = command.substring(5); //number
//            Task t = arr.get(Integer.parseInt(taskIndex) - 1);
//            if (t.getDone().equals("[X]")) {
//                error_code = 9; // task is already marked as not done
//            }
//        } catch (NumberFormatException e) {
//            error_code = 4; // invalid command: mark command followed by char when it was meant to be an int
//        } catch (IndexOutOfBoundsException e) {
//            error_code = 5; // Index out of bounds
//        }
//        return error_code;
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
//        try {
//            String taskIndex = command.substring(7); //number
//            Task t = arr.get(Integer.parseInt(taskIndex) - 1);
//            if (t.getDone().equals("[ ]")) {
//                error_code = 8; // task is already marked as not done
//            }
//        } catch (NumberFormatException e) {
//            error_code = 4; // invalid command: mark command followed by char when it was meant to be an int
//        } catch (IndexOutOfBoundsException e) {
//            error_code = 5; // Index out of bounds
//        }
//
//        return error_code;
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
