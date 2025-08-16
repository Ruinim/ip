public class Event extends Task {
    protected String from;

    public Event(String task, String done, String fm) {
        super(task, done);
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
}
