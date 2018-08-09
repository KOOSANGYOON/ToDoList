package com.todoApp.ToDoApp.domain;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ToDoTest {
    private static final Logger log = LoggerFactory.getLogger(ToDoTest.class);

    private ToDo makeToDo(String title) {
        return new ToDo(title);
    }

    @Test
    public void makeToDoTest() {
        ToDo toDo = makeToDo("todo_one");
        assertEquals(toDo.getTitle(), "todo_one");
        assertEquals(toDo.isDone(), false);
        assertEquals(toDo.isReadyStatus(), true);
        assertEquals(toDo.getReferedToDos().size(), 0);
        assertEquals(toDo.getReferingToDos().size(), 0);
        assertNotEquals(toDo.getFormattedCreatedDate(), "");
    }

    @Test
    public void updateToDoTest() throws InterruptedException {
        ToDo toDo = makeToDo("todo_two");
        assertEquals(toDo.getFormattedCreatedDate(), toDo.getFormattedModifiedDate());

        Thread.sleep(1000);
        toDo.updateModifiedDate();

        assertNotEquals(toDo.getFormattedCreatedDate(), toDo.getFormattedModifiedDate());
    }

    @Test
    public void registerToDoTest() {
        ToDo toDoOne = makeToDo("todo_one");
        ToDo toDoTwo = makeToDo("todo_two");

        toDoOne.registerReferedToDo(toDoTwo);
        toDoTwo.registerReferingToDo(toDoOne);

        assertEquals(toDoOne.getReferedToDos().size(), 1);
        assertEquals(toDoTwo.getReferingToDos().size(), 1);
    }

    @Test
    public void isAlreadyReferedTest() {
        ToDo toDoOne = makeToDo("todo_one");
        ToDo toDoTwo = makeToDo("todo_two");

        toDoOne.registerReferedToDo(toDoTwo);
        toDoTwo.registerReferingToDo(toDoOne);

        boolean result = toDoOne.isAlreadyRefered(toDoTwo);
        assertTrue(result);
    }

}