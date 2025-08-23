package Reim;

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
     * @return duplicate of task object but unmarked
     */
    @Override
    public Todo unmark() {
        return new Todo("[ ]", this.task);
    }

    /**
     * marking this task as  done
     * @return duplicate of task object but marked
     */
    @Override
    public Todo mark() {
        return new Todo("[X]", this.task);
    }

    /**
     * String output of object
     * @return String output of our task
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * output of string to be saved into external file
     * @return string output to be saved into external file
     */
    @Override
    public String formattedString() {
        String done = "0";
        if (this.done.equals("[X]")) {
            done = "1";
        }
        return "T | " + done + " | " + this.task;
    }
}