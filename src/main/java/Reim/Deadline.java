package Reim;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Deadline type task
 * @author Ruinim
 */
public class Deadline extends Task {
    /**
     * by is the date of the deadline task
     * time is the time of the deadline task
     */
    private final LocalDate by;
    private final LocalTime time;

    public Deadline(String done, String task, String by) {
        // if no time stated, assume midnight
        super(done, task);
        this.by = LocalDate.parse(by);
        this.time = LocalTime.parse("00:00");
    }

    public Deadline(String done, String task, LocalDate by) {
        super(done, task);
        this.by = by;
        this.time = LocalTime.parse("00:00");
    }

    public Deadline(String done, String task, LocalDate by, LocalTime time) {
        super(done, task);
        this.by = by;
        this.time = time;
    }

    /**
     * marking this task as not done
     * @return duplicate of task object but unmarked
     */
    @Override
    public Deadline unmark() {
        return new Deadline("[ ]", this.task, this.by);
    }

    /**
     * marking this task as done
     * @return duplicate of task object but marked
     */
    @Override
    public Deadline mark() {
        return new Deadline("[X]", this.task, this.by);
    }

    /**
     * String output of object
     * @return String output of our task
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + " " + this.time.format(DateTimeFormatter.ofPattern("HH:mm"))+ ")";
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
        return "D | " + done + " | " + this.task + " | " + this.by + " " + this.time;
    }
}