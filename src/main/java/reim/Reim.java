package reim;

/**
 * Reim is the driver class of the entire application
 * @author Ruinim
 */
public class Reim {
    /**
     * storage is where our class methods of reading initial file and saving entries are located
     * items is the TaskList of items where our entries are
     * ui is the printing class
     */
    private Storage storage;
    private TaskList items;
    private Ui ui;

    /**
     * Constructor method of Reim
     *
     * @param dirPath relative directory path of file to be read from
     * @param filePath relative file path of file to be read and written into
     * @throws ReimException exception handler
     */
    public Reim(String dirPath, String filePath) throws ReimException {
        ui = new Ui();
        storage = new Storage(dirPath, filePath);
        items = new TaskList(storage.readFile());

    }

    /**
     * driver method
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


    public static void main(String[] args) throws ReimException {
        new Reim("src/data", "src/data/reim.txt").run();

    }
}
