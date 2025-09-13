package reim;

/**
 * The {Reim} class serves as the main driver for the application.
 * It initializes the necessary components and handles both command-line and GUI-based execution modes.
 * <p>
 * This class coordinates interactions between the user interface (Ui),
 * the task list (TaskList), and the storage component (Storage).
 * </p>
 *
 * @author Ruinim
 */
public class Reim {
    /**
     * Handles reading from and writing to the storage file.
     */
    private Storage storage;
    /**
     * Stores the list of tasks in the application.
     */
    private TaskList items;
    /**
     * Manages user interaction including input and output.
     */
    private Ui ui;

    /**
     *  Constructs a Reim instance by initializing the UI, loading tasks from storage
     *  and setting up the internal task list.
     *
     * @param dirPath relative directory path of file to be read from
     * @param filePath relative file path of file to be read and written into
     * @throws ReimException if there is an error initializing the storage or reading the file
     */
    public Reim(String dirPath, String filePath) throws ReimException {
        ui = new Ui();
        storage = new Storage(dirPath, filePath);
        items = new TaskList(storage.readFile());

    }

    /**
     * Starts the application in manual mode (no GUI).
     * Continuously reads and processes user input until the bye command is issued.
     * Handles command parsing, error checking, task list updates, and file saving.
     */
    public void run() {
        ui.start();

        while (ui.hasMoreInput()) {
            String command = ui.showInputLine();
            if (command.equals("bye")) {
                ui.end();
                break;
            }
            Parser parser = new Parser(command, items);
            Integer error = parser.errorInCommand();

            if (error > 0) {
                ui.printError(new ReimException(error, command));
                continue;
            }

            String output = parser.action();
            if (output.isEmpty()) {
                String addition = parser.addList();
                output = "Got it. I've added this task:\n" + addition
                        + "\nNow you have " + items.getSize() + " task(s) in the list.";
                // save list into ./data/Reim.Reim.txt

            }

            storage.saveArray(items);
            ui.printOutput(output);
        }
    }

    /**
     * Processes a user command and returns the appropriate response.
     * This method is used in GUI mode to handle user input and return feedback as a string.
     * It performs validation, executes actions, updates storage, and formats the output.
     *
     * @param command the user command to be parsed and executed
     * @return a string containing the response to the user
     */
    public String getResponse(String command) {
        if (command.equals("bye")) {
            ui.end();
            return ui.generateEndStatement();
        }
        Parser parser = new Parser(command, items);
        Integer error = parser.errorInCommand();

        if (error > 0) {
            ui.printError(new ReimException(error, command));
            return ui.processErrorOutput(new ReimException(error, command));
        }

        String output = parser.action();
        if (output.isEmpty()) {
            String addition = parser.addList();
            output = "Got it. I've added this task:\n" + addition
                    + "\nNow you have " + items.getSize() + " task(s) in the list.";
            // save list into ./data/Reim.Reim.txt
        }

        storage.saveArray(items);
        ui.printOutput(output);
        return ui.processNormalOutput(output);
    }

    /**
     * The application's entry point. Initializes and runs the application in command-line mode.
     *
     * @param args command-line arguments (not used)
     * @throws ReimException if an error occurs during initialization or execution
     */
    public static void main(String[] args) throws ReimException {
        new Reim("src/data", "src/data/reim.txt").run();

    }
}
