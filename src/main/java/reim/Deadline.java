package reim;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that has a deadline, which includes both a date and optional time.
 * This is a subclass of Task.
 * @author Ruinim
 */
public class Deadline extends Task {
    /** The date of the deadline. */
    private final LocalDate deadlineDate;
    /** The time of the deadline. Defaults to 00:00 if not specified. */
    private final LocalTime deadlineTime;

    /**
     * Constructor method of Deadline for String, String, String
     *
     * @param isDone done status of task
     * @param task description of task
     * @param by String of when the deadline is to be converted to LocalDate
     */
    public Deadline(boolean isDone, String task, String by) {
        // if no time stated, assume midnight
        super(isDone, task);
        this.deadlineDate = LocalDate.parse(by);
        this.deadlineTime = LocalTime.parse("00:00");
    }

    /**
     * Constructor method of Deadline for String, String, LocalDate
     *
     * @param isDone done status of task
     * @param task description of task
     * @param by when the deadline is
     */
    public Deadline(boolean isDone, String task, LocalDate by) {
        super(isDone, task);
        this.deadlineDate = by;
        this.deadlineTime = LocalTime.parse("00:00");
    }

    /**
     * constructor method of Deadline for String, String, LocalDate, LocalTime
     *
     * @param isDone done status of task
     * @param task description of task
     * @param by when the deadline is
     * @param time what time is the deadline
     */
    public Deadline(boolean isDone, String task, LocalDate by, LocalTime time) {
        super(isDone, task);
        this.deadlineDate = by;
        this.deadlineTime = time;
    }

    /**
     * Returns a new Deadline instance identical to this one,
     * but marked as not done.
     *
     * @return a copy of this task marked as not done
     */
    @Override
    public Deadline unmark() {
        return new Deadline(false, this.task, this.deadlineDate, this.deadlineTime);
    }

    /**
     * Returns a new Deadline instance identical to this one,
     * but marked as done.
     *
     * @return a copy of this task marked as done
     */
    @Override
    public Deadline mark() {
        return new Deadline(true, this.task, this.deadlineDate, this.deadlineTime);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.deadlineDate
                .format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
                + " " + this.deadlineTime.format(DateTimeFormatter.ofPattern("HH:mm")) + ")";
    }

    /**
     * Generates a formatted string representation of this task for file storage.
     * Format: { D | 1/0 | task description | yyyy-MM-dd HH:mm}
     *
     * @return a machine-readable string representation of the task
     */
    @Override
    public String generateFormattedString() {
        String done = "0";
        if (this.isDone) {
            done = "1";
        }
        return "D | " + done + " | " + this.task + " | " + this.deadlineDate + " " + this.deadlineTime;
    }
}
