package reim;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class StorageTest {

    @Test
    public void saveArrayTest() throws ReimException {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Event(false, "party", "2022-12-18"));
        tasks.add(new Deadline(true, "test", LocalDate.parse("2022-10-08"), LocalTime.parse("10:00")));
        tasks.add(new Todo(false, "temp"));
        TaskList t = new TaskList(tasks);
        Storage store = new Storage("src/test/data/reim", "src/test/data/reim/testFile.txt");
        assertEquals(t.getArray().toString(), store.readFile().getArray().toString());
    }

    @Test
    public void emptyListTest() throws ReimException {
        ArrayList<Task> tasks = new ArrayList<>();
        TaskList t = new TaskList(tasks);
        Storage store = new Storage("src/test/data/reim", "src/test/data/reim/emptyFile.txt");
        assertEquals(t.getArray().toString(), store.readFile().getArray().toString());
    }

}
