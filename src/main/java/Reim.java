import java.util.Scanner;

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
        while (!stop) {
            Scanner read = new Scanner(System.in);
            String command = read.nextLine();
            String output = message(command);
            if (command.equals("bye")) {
                output = end;
                stop = true;
            }
            System.out.println(output);
        }




    }

    public static String message(String msg) {
        return "____________________________________________________________\n"
                + msg + "\n"
                + "____________________________________________________________\n";
    }
}
