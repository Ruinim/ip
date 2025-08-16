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
        ArrayList<String> items = new ArrayList<String>();
        while (!stop) {
            Scanner read = new Scanner(System.in);
            String command = read.nextLine();
            String output = message("added: " + command);
            Integer commandType = commandParse(command);

            if (commandType.equals(1)) {
                output = end;
                stop = true;
            } else if (commandType.equals(2)) {
                output = message(listOutput(items));
            }
            else {
                // not any of the commands --> add to list
                items.add(command);
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
        return 0;
    }

    public static String listOutput(ArrayList<String> arr) {
        StringBuilder finalString = new StringBuilder();
        for (int i = 1; i - 1 < arr.size(); i++) {
            finalString.append(i).append(". ").append(arr.get(i - 1)).append("\n");
        }
        return finalString.toString();
    }
}
