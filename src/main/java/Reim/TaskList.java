package Reim;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * object wrapping around arraylist of tasks
 * @author Ruinim
 */
public class TaskList {
    /**
     * tasks is the arraylist of tasks to wrap
     */
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList(TaskList t) {
        this.tasks = t.getArray();
    }

    /**
     * get array data of tasklist
     * @return arraylist of tasks
     */
    public ArrayList<Task> getArray() {
        return this.tasks;
    }

    /**
     * adding to the tasklist
     * @param t is task to be added
     */
    public void add(Task t) {
        this.tasks.add(t);
    }

    /**
     * revealing size of tasklist
     * @return size of tasklist
     */
    public int size() {
        return this.tasks.size();
    }

    /**
     * obtaining task at index i
     * @param i is index to be checked
     * @return task at index given
     */
    public Task get(int i) {
        return this.tasks.get(i);
    }

    /**
     * setting new task at specified index i
     * @param i index to be swapped
     * @param t is new task to be swapped in
     */
    public void set(int i, Task t) {
        this.tasks.set(i, t);
    }

    /**
     * remove task
     * @param i is index of task to be removed
     */
    public void remove(int i) {
        this.tasks.remove(i);
    }

    public TaskList searchList(String s) {
        Stream<Task> stream = getArray().stream().filter(x -> x.getTask().contains(s));
        return new TaskList(new ArrayList<Task>(stream.collect(Collectors.toList())));
    }
}
