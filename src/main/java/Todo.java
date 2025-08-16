public class Todo extends Task {

    public Todo(String task, String done) {
        super(task, done);
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
}