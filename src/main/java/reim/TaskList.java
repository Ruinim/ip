package reim;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A wrapper class around an arraylist of tasks that provides
 * utility methods for managing a list of tasks.
 * This class supports adding, removing, updating, and searching tasks.
 *
 * @author Ruinim
 */
public class TaskList {
    /**
     * Internal list that stores the tasks.
     */
    private ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with an existing list of tasks.
     *
     * @param tasks the list of tasks to wrap
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Constructs a new TaskList by copying the internal task list
     * of another TaskList instance.
     *
     * @param t the TaskList to copy
     */
    public TaskList(TaskList t) {
        this.tasks = t.getArray();
    }

    /**
     * get array data of tasklist
     *
     * @return arraylist of tasks
     */
    public ArrayList<Task> getArray() {
        return this.tasks;
    }

    /**
     * adding to the tasklist
     *
     * @param t is task to be added
     */
    public void add(Task t) {
        this.tasks.add(t);
    }

    /**
     * revealing size of tasklist
     *
     * @return size of tasklist
     */
    public int getSize() {
        return this.tasks.size();
    }

    /**
     * obtaining task at index i
     *
     * @param index is index to be checked
     * @return task at index given
     */
    public Task get(int index) {
        assert index < this.tasks.size();
        return this.tasks.get(index);
    }

    /**
     * setting new task at specified index i
     *
     * @param index index to be swapped
     * @param task is new task to be swapped in
     */
    public void set(int index, Task task) {
        assert index < this.tasks.size();
        this.tasks.set(index, task);
    }

    /**
     * remove task
     *
     * @param i is index of task to be removed
     */
    public void remove(int i) {
        this.tasks.remove(i);
    }

    /**
     * Generate a filtered taskList containing only entries that contain the string we are searching for
     *
     * @param searchString is the search criteria of this search
     * @return Filtered tasklist of entries that contain s
     */
    public TaskList searchList(String searchString) {
        Stream<Task> stream = getArray().stream().filter(x -> x.getTask().startsWith(searchString));
        return new TaskList(new ArrayList<Task>(stream.collect(Collectors.toList())));
    }
}
