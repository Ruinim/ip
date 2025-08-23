package Reim;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskList {
    protected ArrayList<Task> tasks;

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

    public TaskList searchList(String s) {
        Stream<Task> stream = getArray().stream().filter(x -> x.getTask().contains(s));
        return new TaskList(new ArrayList<Task>(stream.collect(Collectors.toList())));
    }
}
