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

    /**
     * Constructor method of Deadline for String, String, String
     *
     * @param done done status of task
     * @param task description of task
     * @param by String of when the deadline is to be converted to LocalDate
     */
    public Deadline(String done, String task, String by) {
        // if no time stated, assume midnight
        super(done, task);
        this.by = LocalDate.parse(by);
        this.time = LocalTime.parse("00:00");
    }

    /**
     * Constructor method of Deadline for String, String, LocalDate
     *
     * @param done done status of task
     * @param task description of task
     * @param by when the deadline is
     */
    public Deadline(String done, String task, LocalDate by) {
        super(done, task);
        this.by = by;
        this.time = LocalTime.parse("00:00");
    }

    /**
     * constructor method of Deadline for String, String, LocalDate, LocalTime
     *
     * @param done done status of task
     * @param task description of task
     * @param by when the deadline is
     * @param time what time is the deadline
     */
    public Deadline(String done, String task, LocalDate by, LocalTime time) {
        super(done, task);
        this.by = by;
        this.time = time;
    }

    @Override
    public Deadline unmark() {
        return new Deadline("[ ]", this.task, this.by);
    }

    @Override
    public Deadline mark() {
        return new Deadline("[X]", this.task, this.by);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
                + " " + this.time.format(DateTimeFormatter.ofPattern("HH:mm")) + ")";
    }

    @Override
    public String generateFormattedString() {
        String done = "0";
        if (this.done.equals("[X]")) {
            done = "1";
        }
        return "D | " + done + " | " + this.task + " | " + this.by + " " + this.time;
    }
}
