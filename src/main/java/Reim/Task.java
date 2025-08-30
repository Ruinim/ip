package Reim;

/**
 * Our individual entries
 * @author Ruinim
 */
public class Task {
    /**
     * task is the task that we need to do
     * done is if we have done this task ([X], [ ])
     */
    protected String task;
    protected String done;

    /**
     * Constructor method of Task
     *
     * @param d done status of Task
     * @param t description of Task
     */
    public Task(String d, String t) {
        this.task = t;
        this.done = d;
    }

    /**
     * get task string
     *
     * @return task string of object
     */
    public String getTask() {
        return this.task;
    }

    /**
     * get done string
     *
     * @return done string of object
     */
    public String getDone() {
        return this.done;
    }

    /**
     * marking this task as not done
     *
     * @return duplicate of task object but unmarked
     */
    public Task unmark() {
        return new Task("[ ]", this.task);
    }

    /**
     * marking this task as done
     *
     * @return duplicate of task object but marked
     */
    public Task mark() {
        return new Task("[X]", this.task);
    }

    @Override
    public String toString() {
        return this.done + " " + this.task;
    }

    /**
     * Generate output of Task for it to be saved into external file
     *
     * @return string output of event
     */
    public String generateFormattedString() {
        return "";
    }
}
