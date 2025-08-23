package Reim;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    protected LocalDate by;
    protected LocalTime time;

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
        return "[D]" + super.toString() + " (by: " + this.by.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + " " + this.time.format(DateTimeFormatter.ofPattern("HH:mm"))+ ")";
    }

    @Override
    public String formattedString() {
        String done = "0";
        if (this.done.equals("[X]")) {
            done = "1";
        }
        return "D | " + done + " | " + this.task + " | " + this.by + " " + this.time;
    }
}