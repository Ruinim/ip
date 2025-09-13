package reim;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that has an event, which includes both a date and optional time.
 * This is a subclass of Task.
 * @author Ruinim
 */
public class Event extends Task {
    /** The date when the event starts. */
    private final LocalDate startDate;
    /** The time when the event starts. Defaults to 00:00 if not specified. */
    private final LocalTime startTime;

    /**
     * Constructor method of Event for String, String and LocalDate
     *
     * @param isDone done status of task
     * @param task description of task
     * @param fromTime from when does the event start
     */
    public Event(boolean isDone, String task, LocalDate fromTime) {
        super(isDone, task);
        this.startDate = fromTime;
        this.startTime = LocalTime.parse("00:00");
    }

    /**
     * Constructor method of Event for String, String and String
     *
     * @param isDone done status of task
     * @param task description of task
     * @param fromTime from when does the event start to be converted to LocalDate
     */
    public Event(boolean isDone, String task, String fromTime) {
        super(isDone, task);
        this.startDate = LocalDate.parse(fromTime);
        this.startTime = LocalTime.parse("00:00");
    }

    /**
     * Constructor method of Event for String, String, LocalDate and LocalTime
     *
     * @param isDone done status of task
     * @param task description of task
     * @param fromTime from when does the event start
     * @param time what time does the event start
     */
    public Event(boolean isDone, String task, LocalDate fromTime, LocalTime time) {
        super(isDone, task);
        this.startDate = fromTime;
        this.startTime = time;
    }

    /**
     * Returns a new Event instance identical to this one,
     * but marked as not done.
     *
     * @return a copy of this task marked as not done
     */
    @Override
    public Event unmark() {
        return new Event(false, this.task, this.startDate, this.startTime);
    }

    /**
     * Returns a new Event instance identical to this one,
     * but marked as done.
     *
     * @return a copy of this task marked as done
     */
    @Override
    public Event mark() {
        return new Event(true, this.task, this.startDate, this.startTime);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.startDate.format(DateTimeFormatter.ofPattern("MMM dd yyyy"))
                + " " + this.startTime.format(DateTimeFormatter.ofPattern("HH:mm")) + ")";
    }

    /**
     * Generates a formatted string representation of this task for file storage.
     * Format: { E | 1/0 | task description | yyyy-MM-dd HH:mm}
     *
     * @return a machine-readable string representation of the task
     */
    @Override
    public String generateFormattedString() {
        String done = "0";
        if (this.isDone) {
            done = "1";
        }
        return "E | " + done + " | " + this.task + " | " + this.startDate + " " + this.startTime;
    }
}
