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
     * storage is where our class methods of reading initial file and saving entries are located
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
}
