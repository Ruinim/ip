public class Ui {

    public Ui() {
    }


    public static String message(String msg) {
        return "____________________________________________________________\n"
                + msg + "\n"
                + "____________________________________________________________\n";
    }

    public void start() {
        System.out.println("""
                ____________________________________________________________
                Hello! I'm Reim
                What can I do for you?
                ____________________________________________________________
                """);
    }

    public void end() {
        System.out.println("""
                ____________________________________________________________
                Bye. Hope to see you again soon!
                ____________________________________________________________
                """);
    }

    public static String errorOutput(ReimException e) {
        return message(e.errorMessage());
    }

    public static String normalOutput(String s) {
        return message(s);
    }

    public void printOutput(String s) {
        System.out.println(normalOutput(s));
    }

    public void printError(ReimException e) {
        System.out.println(errorOutput(e));
    }
}
