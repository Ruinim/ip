public class Deadline extends Task {

    protected String by;

    public Deadline(String task, String done, String by) {
        super(task, done);
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
}