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
        ArrayList<Task> items = new ArrayList<>();
        while (true) {
            Scanner read = new Scanner(System.in);
            String command = read.nextLine();
            if (command.equals("bye")) {
                System.out.println(end);
                break;
            }
            String output = action(command, items);
            if (output.isEmpty()){
                output = addingList(command, items);
            }
            System.out.println(output);
        }




    }

    public static String addingList(String command, ArrayList<Task> arr) {
        if (command.startsWith("todo ")) {
            arr.add(new Todo("[ ]", command.substring(5)));
            return new Todo("[ ]", command.substring(5)).toString();
        }
        else if (command.startsWith("deadline ")) {
            int index = command.indexOf("/");
            String task = command.substring(9, index);
            String deadline = command.substring(index+ 3);
            arr.add(new Deadline("[ ]", task, deadline));
            return new Deadline("[ ]", task, deadline).toString();
        }
        else if (command.startsWith("event ")) {
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
        else if (command.startsWith("mark ")) {
            return 3;
        }
        else if (command.startsWith("unmark ")) {
            return 4;
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
        else if (commandType.equals(4)) {
            String taskIndex = command.substring(7); //number
            Task t = arr.get(Integer.parseInt(taskIndex) - 1);
            arr.set(Integer.parseInt(taskIndex) - 1, t.unmark());
            finalOutput = message("OK, I've marked this task as not done yet:\n" + t.unmark());
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
}
