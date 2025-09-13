package reim;

/**
 * Represents a task that has a todo
 * This is a subclass of Task.
 * @author Ruinim
 */
public class Todo extends Task {

    public Todo(boolean done, String task) {
        super(done, task);
    }

    /**
     * Returns a new Todo instance identical to this one,
     * but marked as not done.
     *
     * @return a copy of this task marked as not done
     */
    @Override
    public Todo unmark() {
        return new Todo(false, this.task);
    }

    /**
     * Returns a new Todo instance identical to this one,
     * but marked as done.
     *
     * @return a copy of this task marked as done
     */
    @Override
    public Todo mark() {
        return new Todo(true, this.task);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Generates a formatted string representation of this task for file storage.
     * Format: { T | 1/0 | task description }
     *
     * @return a machine-readable string representation of the task
     */
    @Override
    public String generateFormattedString() {
        String done = "0";
        if (this.isDone) {
            done = "1";
        }
        return "T | " + done + " | " + this.task;
    }
}
