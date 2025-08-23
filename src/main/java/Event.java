import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    protected LocalDate from;
    protected LocalTime time;

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

    @Override
    public Event unmark() {
        return new Event("[ ]", this.task, this.from);
    }

    @Override
    public Event mark() {
        return new Event("[X]", this.task, this.from);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from.format(DateTimeFormatter.ofPattern("MMM dd yyyy")) + " " + this.time.format(DateTimeFormatter.ofPattern("HH:mm")) + ")";
    }

    @Override
    public String formattedString() {
        String done = "0";
        if (this.done.equals("[X]")) {
            done = "1";
        }
        return "E | " + done + " | " + this.task + " | " + this.from + " " + this.time;
    }
}
