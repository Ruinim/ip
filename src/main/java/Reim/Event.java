package Reim;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Event type task
 * @author Ruinim
 */
public class Event extends Task {
    private final LocalDate from;
    private final LocalTime time;

    public Event(String done, String task, LocalDate fm) {
        super(done, task);
        this.from = fm;
        this.time = LocalTime.parse("00:00");
    }

    public Event(String done, String task, String fm) {
        super(done, task);
        this.from = LocalDate.parse(fm);
        this.time = LocalTime.parse("00:00");
    }

    public Event(String done, String task, LocalDate fm, LocalTime time) {
        super(done, task);
        this.from = fm;
        this.time = time;
    }

    /**
     * marking this task as not done
     * @return duplicate of task object but unmarked
     */
    @Override
    public Event unmark() {
        return new Event("[ ]", this.task, this.from);
    }

    /**
     * marking this task as done
     * @return duplicate of task object but marked
     */
    @Override
    public Event mark() {
        return new Event("[X]", this.task, this.from);
    }

    /**
     * String output of object
     * @return String output of our task
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + " " + this.time.format(DateTimeFormatter.ofPattern("HH:mm")) + ")";
    }

    /**
     * output of string to be saved into external file
     * @return string output to be saved into external file
     */
    @Override
    public String formattedString() {
        String done = "0";
        if (this.done.equals("[X]")) {
            done = "1";
        }
        return "E | " + done + " | " + this.task + " | " + this.from + " " + this.time;
    }
}
