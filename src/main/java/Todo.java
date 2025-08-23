public class Todo extends Task {

    public Todo(String done, String task) {
        super(done, task);
    }

    @Override
    public Todo unmark() {
        return new Todo("[ ]", this.task);
    }

    @Override
    public Todo mark() {
        return new Todo("[X]", this.task);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String formattedString() {
        String done = "0";
        if (this.done.equals("[X]")) {
            done = "1";
        }
        return "T | " + done + " | " + this.task;
    }
}