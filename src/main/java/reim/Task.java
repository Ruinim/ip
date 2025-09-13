package reim;

/**
 * Represents a general task with a description and completion status.
 * This is the base class for more specific task types such as Todo, Deadline, and Event.
 * Each Task object is immutable; marking or unmarking returns a new Task instance.
 *
 * @author Ruinim
 */
public class Task {
    /**
     * The description of the task.
     */
    protected String task;
    /**
     * Indicates whether the task is marked as done.
     */
    protected boolean isDone;

    /**
     * Constructs a Task with the given completion status and description.
     *
     * @param d true if the task is completed; false otherwise
     * @param t description of Task
     */
    public Task(boolean d, String t) {
        this.task = t;
        this.isDone = d;
    }

    /**
     * Returns the description of the task.
     *
     * @return the task description
     */
    public String getTask() {
        return this.task;
    }

    /**
     * Returns the completion status of the task.
     *
     * @return true if the task is marked as done; false otherwise
     */
    public boolean getDone() {
        return this.isDone;
    }

    /**
     * Returns a new Task instance representing this task marked as not done.
     *
     * @return a new Task object with the same description, marked as not done
     */
    public Task unmark() {
        return new Task(false, this.task);
    }

    /**
     * Returns a new Task instance representing this task marked as done.
     *
     * @return a new Task object with the same description, marked as done
     */
    public Task mark() {
        return new Task(true, this.task);
    }

    @Override
    public String toString() {
        return (this.isDone ? "[X]" : "[ ]") + " " + this.task;
    }

    /**
     * Generates a formatted string representing this task for saving to a file.
     * This method is intended to be overridden by subclasses.
     *
     * @return the formatted string for persistent storage
     */
    public String generateFormattedString() {
        return "";
    }
}
