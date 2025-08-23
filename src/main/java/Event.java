public class Event extends Task {
    protected String from;

    public Event(String done, String task, String fm) {
        super(done, task);
        this.from = fm;
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
        return "[E]" + super.toString() + " (from: " + from + ")";
    }

    @Override
    public String formattedString() {
        String done = "0";
        if (this.done.equals("[X]")) {
            done = "1";
        }
        return "E | " + done + " | " + this.task + " | " + this.from;
    }
}
