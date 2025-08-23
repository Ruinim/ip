public class Deadline extends Task {

    protected String by;

    public Deadline(String done, String task, String by) {
        super(done, task);
        this.by = by;
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
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String formattedString() {
        String done = "0";
        if (this.done.equals("[X]")) {
            done = "1";
        }
        return "D | " + done + " | " + this.task + " | " + this.by;
    }
}