package reim;

/**
 * todo type task
 * @author Ruinim
 */
public class Todo extends Task {

    public Todo(String done, String task) {
        super(done, task);
    }

    /**
     * marking this task as not done
     *
     * @return duplicate of task object but unmarked
     */
    @Override
    public Todo unmark() {
        return new Todo("[ ]", this.task);
    }

    /**
     * marking this task as done
     *
     * @return duplicate of task object but marked
     */
    @Override
    public Todo mark() {
        return new Todo("[X]", this.task);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Generate output of Task for it to be saved into external file
     *
     * @return string output of event
     */
    @Override
    public String generateFormattedString() {
        String done = "0";
        if (this.done.equals("[X]")) {
            done = "1";
        }
        return "T | " + done + " | " + this.task;
    }
}
