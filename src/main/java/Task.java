public class Task {
    protected String task;
    protected String done;

    public Task(String d, String t) {
        this.task = t;
        this.done = d;
    }

    public String getTask() {
        return this.task;
    }

    public String getDone() {
        return this.done;
    }

    public Task unmark() {
        return new Task("[ ]", this.task);
    }

    public Task mark() {
        return new Task("[X]", this.task);
    }

    @Override
    public String toString() {
        return this.done + " " + this.task;
    }

    public String formattedString() {
        return "";
    }
}
