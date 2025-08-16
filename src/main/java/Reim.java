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
            Integer error = errorInCommand(command, items);
            if (error > 0) {
                System.out.println(new ReimException(error, command));
                continue;
            }
            String output = action(command, items);
            if (output.isEmpty()){
                String addition = addingList(command, items);
                output = message("Got it. I've added this task:\n" + addition
                        + "\nNow you have " + items.size() + " task(s) in the list.");
            }
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
                // todo doesnt have errors what wouldnt be caught by the first check
                else if (command.startsWith("deadline")) {
                    error_code = deadlineCheck(command, error_code);

                }
                else if (command.startsWith("event")) {
                    error_code = eventCheck(command, error_code);
                }

            }
        }
        if (count < 1) {
            return 1; //invalid command: please use the commands list, todo event, deadline, mark, unmark
        }
        return error_code;
    }

    public static Integer markCheck(String command, ArrayList<Task> arr, Integer error_code) {
        try {
            String taskIndex = command.substring(5); //number
            Task t = arr.get(Integer.parseInt(taskIndex) - 1);
        } catch (NumberFormatException e) {
            error_code = 4; // invalid command: mark command followed by char when it was meant to be an int
        } catch (IndexOutOfBoundsException e) {
            error_code = 5; // Index out of bounds
        }
        return error_code;
    }

    public static Integer unmarkCheck(String command, ArrayList<Task> arr, Integer error_code) {
        try {
            String taskIndex = command.substring(7); //number
            Task t = arr.get(Integer.parseInt(taskIndex) - 1);
        } catch (NumberFormatException e) {
            error_code = 4; // invalid command: mark command followed by char when it was meant to be an int
        } catch (IndexOutOfBoundsException e) {
            error_code = 5; // Index out of bounds
        }
        return error_code;
    }

    public static Integer deadlineCheck(String command, Integer error_code) {
        if(!command.contains("/by")) { //no /by
            return 6; // invalid arguments: no timing given
        }
        int index = command.indexOf("/");
        if (command.substring(9, index).isEmpty()) {
            return 7; // invalid argument: no task given in command
        }
        else if (command.substring(index+ 3).isEmpty()) {
            return 6;
        }
        return error_code;
    }

    public static Integer eventCheck(String command, Integer error_code) {
        if (!command.contains("/from")) {
            return 6; // invalid arguments: no timing given
        }
        int index = command.indexOf("/");
        if (command.substring(6, index).isEmpty()) {
            return 7; // invalid argument: no task given in command
        }
        else if (command.substring(index + 5).isEmpty()) {
            return 6;
        }
        return error_code;
    }

    public static Integer deleteCheck(String command, ArrayList<Task> arr, Integer error_code) {
        try {
            String taskIndex = command.substring(7); //number
            Task t = arr.get(Integer.parseInt(taskIndex) - 1);
        } catch (NumberFormatException e) {
            error_code = 4; // invalid command: mark command followed by char when it was meant to be an int
        } catch (IndexOutOfBoundsException e) {
            error_code = 5; // Index out of bounds
        }
        return error_code;
    }
}
