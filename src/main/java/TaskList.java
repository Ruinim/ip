import java.util.ArrayList;

public class TaskList {
    protected ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> t) {
        this.tasks = t;
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

    public Task get(String i) {
        return this.tasks.get(Integer.parseInt(i) - 1);
    }

    public Task get(int i) {
        return this.tasks.get(i - 1);
    }

    public void set(int i, Task t) {
        this.tasks.set(i, t);
    }

    public void remove(int i) {
        this.tasks.remove(i);
    }
}
