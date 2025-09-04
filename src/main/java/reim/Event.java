package reim;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Event type task
 * @author Ruinim
 */
public class Event extends Task {
    private final LocalDate startDate;
    private final LocalTime startTime;

    /**
     * Constructor method of Event for String, String and LocalDate
     *
     * @param done done status of task
     * @param task description of task
     * @param fm from when does the event start
     */
    public Event(boolean done, String task, LocalDate fm) {
        super(done, task);
        this.startDate = fm;
        this.startTime = LocalTime.parse("00:00");
    }

    /**
     * Constructor method of Event for String, String and String
     *
     * @param done done status of task
     * @param task description of task
     * @param fm from when does the event start to be converted to LocalDate
     */
    public Event(boolean done, String task, String fm) {
        super(done, task);
        this.startDate = LocalDate.parse(fm);
        this.startTime = LocalTime.parse("00:00");
    }

    /**
     * Constructor method of Event for String, String, LocalDate and LocalTime
     *
     * @param done done status of task
     * @param task description of task
     * @param fm from when does the event start
     * @param time what time does the event start
     */
    public Event(boolean done, String task, LocalDate fm, LocalTime time) {
        super(done, task);
        this.startDate = fm;
        this.startTime = time;
    }

    /**
     * marking this task as not done
     *
     * @return duplicate of task object but unmarked
     */
    @Override
    public Event unmark() {
        return new Event(false, this.task, this.startDate, this.startTime);
    }

    /**
     * marking this task as done
     *
     * @return duplicate of task object but marked
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
     * Generate output of Task for it to be saved into external file
     *
     * @return string output of event
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
