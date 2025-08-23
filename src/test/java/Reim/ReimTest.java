package Reim;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReimTest {
    @Test
    public void errorInCommandTest(){
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("[ ]", "borrow"));
        tasks.add(new Deadline("[ ]", "test", "2002-12-12"));
        tasks.add(new Event("[X]", "party", "2002-12-12"));
        TaskList t = new TaskList(tasks);
        Parser parse = new Parser("todo run", t);
        Integer error = parse.errorInCommand();
        assertEquals(0, error);
        assertEquals(1, new Parser("tomooror", t).errorInCommand());
        assertEquals(2, new Parser("todo", t).errorInCommand());
        assertEquals(3, new Parser("list todos", t).errorInCommand());
        assertEquals(4, new Parser("mark hi", t).errorInCommand());
        assertEquals(5, new Parser("mark 5", t).errorInCommand());
        assertEquals(6, new Parser("deadline assignment ", t).errorInCommand());
        assertEquals(7, new Parser("event /from 2002-12-12", t).errorInCommand());
        assertEquals(8, new Parser("unmark 1", t).errorInCommand());
        assertEquals(9, new Parser("mark 3", t).errorInCommand());
        assertEquals(10, new Parser("todo borrow", t).errorInCommand());
        assertEquals(11, new Parser("deadline assignment /by 2200-12-hi", t).errorInCommand());
    }

    @Test
    public void saveArrayTest(){
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Event("[ ]", "party", "2022-12-18"));
        tasks.add(new Deadline("[X]", "test", LocalDate.parse("2022-10-08"), LocalTime.parse("10:00")));
        tasks.add(new Todo("[ ]", "temp"));
        TaskList t = new TaskList(tasks);
        Storage store = new Storage("src/test/data/Reim", "src/test/data/Reim/testFile.txt");
        assertEquals(t.getArray().toString(), store.readFile().getArray().toString());
    }
}