import java.util.Scanner;
import java.util.ArrayList;

public class Reim {
    public static void main(String[] args) {
        String logo = "____________________________________________________________\n"
                + "Hello! I'm Reim\n"
                + "What can I do for you?\n"
                + "____________________________________________________________\n";
        String end = "____________________________________________________________\n"
                + "Bye. Hope to see you again soon!\n"
                + "____________________________________________________________\n";
        System.out.println(logo);

        boolean stop = false;
        ArrayList<Task> items = new ArrayList<Task>();
        while (true) {
            Scanner read = new Scanner(System.in);
            String command = read.nextLine();
            if (command.equals("bye")) {
                System.out.println(end);
                break;
            }
            String output = action(command, items);
            if (output.isEmpty()){
                // not any of the commands --> add to list
                items.add(new Task("[ ]", command));
                output = message("added: " + command);
            }
            System.out.println(output);
        }




    }

    public static String message(String msg) {
        return "____________________________________________________________\n"
                + msg + "\n"
                + "____________________________________________________________\n";
    }

    public static Integer commandParse(String command) {
        if (command.equals("bye")) {
            return 1;
        }
        else if (command.equals("list")) {
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
            System.out.println("testing");
            finalOutput = message(listOutput(arr));
        }
        else if (commandType.equals(3)) { //mark
            String taskIndex = command.substring(5); //number
            Task t = arr.get(Integer.parseInt(taskIndex) - 1);
            arr.set(Integer.parseInt(taskIndex) - 1, t.mark());
            finalOutput = message("Nice! I've marked this task as done:\n[X]" + t.getTask());
        }
        else if (commandType.equals(4)) {
            String taskIndex = command.substring(7); //number
            Task t = arr.get(Integer.parseInt(taskIndex) - 1);
            arr.set(Integer.parseInt(taskIndex) - 1, t.unmark());
            finalOutput = message("OK, I've marked this task as not done yet:\n[ ]" + t.getTask());
        }
        return finalOutput;
    }

    public static String listOutput(ArrayList<Task> arr) {
        StringBuilder finalString = new StringBuilder();
        for (int i = 1; i - 1 < arr.size(); i++) {
            finalString.append(i).append(". ").append(arr.get(i - 1).getDone())
                    .append(" ").append(arr.get(i - 1).getTask()).append("\n");
        }
        return finalString.toString();
    }
}
