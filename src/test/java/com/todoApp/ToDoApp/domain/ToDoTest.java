package com.todoApp.ToDoApp.domain;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        assertEquals(toDo.isReadyState(), true);
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
    public void registerToDoTest() throws Exception {
        ToDo toDoOne = makeToDo("todo_one");
        ToDo toDoTwo = makeToDo("todo_two");

        toDoOne.registerReferingToDo(toDoTwo);
        toDoTwo.registerReferedToDo(toDoOne);

        assertEquals(toDoOne.getReferingToDos().size(), 1);
        assertEquals(toDoTwo.getReferedToDos().size(), 1);

        assertTrue(toDoOne.isReadyState());
        assertFalse(toDoTwo.isReadyState());
    }

    @Test(expected = Exception.class)
    public void registerToDoTest_alreadyRegistered() throws Exception {
        ToDo toDoOne = makeToDo("todo_one");
        ToDo toDoTwo = makeToDo("todo_two");

        toDoOne.registerReferingToDo(toDoTwo);
        toDoTwo.registerReferedToDo(toDoOne);
        toDoTwo.registerReferingToDo(toDoOne);
    }

    @Test(expected = Exception.class)
    public void registerToDoTest_alreadyRegistering() throws Exception {
        ToDo toDoOne = makeToDo("todo_one");
        ToDo toDoTwo = makeToDo("todo_two");

        toDoOne.registerReferingToDo(toDoTwo);
        toDoOne.registerReferingToDo(toDoTwo);
        toDoTwo.registerReferedToDo(toDoOne);
    }

    @Test
    public void completeToDoTest() throws Exception {
        ToDo toDoOne = makeToDo("todo_one");
        ToDo toDoTwo = makeToDo("todo_two");

        toDoOne.registerReferingToDo(toDoTwo);
        toDoTwo.registerReferedToDo(toDoOne);

        toDoOne.complete();

        assertTrue(toDoOne.isReadyState());
        assertTrue(toDoTwo.isReadyState());
        log.debug(toDoTwo.getReferedToDos().get(0).isDone() + "");
    }

    @Test
    public void notCompleteToDoTest() throws Exception {
        ToDo toDoOne = makeToDo("todo_one");
        ToDo toDoTwo = makeToDo("todo_two");

        toDoOne.registerReferingToDo(toDoTwo);
        toDoTwo.registerReferedToDo(toDoOne);

        assertFalse(toDoTwo.isReadyState());
        log.debug(toDoTwo.getReferedToDos().get(0).isDone() + "");
    }

    @Test
    public void deleteRefTest_success() throws Exception {
        ToDo toDoOne = makeToDo("todo_one");
        ToDo toDoTwo = makeToDo("todo_two");

        toDoOne.registerReferingToDo(toDoTwo);
        toDoTwo.registerReferedToDo(toDoOne);

        toDoOne.deleteReferingToDo(toDoTwo);
        toDoTwo.deleteReferedToDo((toDoOne));

        assertEquals(toDoOne.getReferingToDos().size(), 0);
        assertEquals(toDoTwo.getReferedToDos().size(), 0);
        assertTrue(toDoOne.isReadyState());
        assertTrue(toDoTwo.isReadyState());
    }

}