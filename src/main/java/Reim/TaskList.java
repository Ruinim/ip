package Reim;

import java.util.ArrayList;

public class TaskList {
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

    public ArrayList<Task> getArray() {
        return this.tasks;
    }

    public void add(Task t) {
        this.tasks.add(t);
    }

    public int size() {
        return this.tasks.size();
    }

    public Task get(int i) {
        return this.tasks.get(i);
    }

    public void set(int i, Task t) {
        this.tasks.set(i, t);
    }

    public void remove(int i) {
        this.tasks.remove(i);
    }
}
