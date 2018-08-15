package com.todoApp.toDoApp.web;

import com.todoApp.toDoApp.domain.ToDo;
import com.todoApp.toDoApp.domain.ToDoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ToDoControllerTest {

    private static final Logger log = LoggerFactory.getLogger(ToDoControllerTest.class);

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private ToDoRepository toDoRepository;

    private ToDo getOne(ToDo toDo) {
        return toDoRepository.findOne(toDo.getId());
    }

    private ToDo makeToDo(String title) {
        ToDo targetToDo = template.postForObject("/api/todos/", title,  ToDo.class);
        return targetToDo;
    }

    private ResponseEntity<String> getResponse(String url, Long id) {
        ResponseEntity<String> response = template.postForEntity(url, id,  String.class);
        return response;
    }

    @Test
    public void createToDoTest_success() {
        String newTitle = "newToDo";
        ResponseEntity<String> response = template.postForEntity("/api/todos/", newTitle,  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
    }

    @Test
    public void createToDoTest_fail_title_not_unique() {
        String newTitle = "todoFail";
        ResponseEntity<String> response = template.postForEntity("/api/todos/", newTitle,  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response2 = template.postForEntity("/api/todos/", newTitle,  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    public void addRefTest_success() {
        //create two todos
        ToDo toDo1 = makeToDo("toDo1");
        ToDo toDo2 = makeToDo("toDo2");

        //make ref & check response
        ResponseEntity<String> response = getResponse(String.format("/api/todos/%d/addref", toDo1.getId()), toDo2.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //check ref is success
        assertEquals(getOne(toDo1).getReferingToDos().getSize(), getOne(toDo2).getReferedToDos().getSize(), 1);
        assertThat(getOne(toDo1).getReferingToDoTitle(0), is(toDo2.getTitle()));   //title == unique
        assertThat(getOne(toDo2).getReferedToDoTitle(0), is(toDo1.getTitle()));   //title == unique
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void addRefTest_fail_bi_direction() {
        //create two todos
        ToDo toDo1 = makeToDo("toDo3");
        ToDo toDo2 = makeToDo("toDo4");

        //make ref & check response
        ResponseEntity<String> response = getResponse(String.format("/api/todos/%d/addref", toDo1.getId()), toDo2.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //make ref & check response (expected = 500 error)
        ResponseEntity<String> response2 = getResponse(String.format("/api/todos/%d/addref", toDo2.getId()), toDo1.getId());
        assertThat(response2.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        //check ref is same at first addref. & second request doesn't reflected.
        assertEquals(getOne(toDo1).getReferingToDos().getSize(), getOne(toDo2).getReferedToDos().getSize(), 1);     //size always 1.
        assertThat(getOne(toDo1).getReferingToDoTitle(0), is(toDo2.getTitle()));
        assertThat(getOne(toDo2).getReferingToDoTitle(0), is(toDo1.getTitle()));
    }

    @Test
    public void addRefTest_fail_addref_double() {
        //create two todos
        ToDo toDo1 = makeToDo("toDo5");
        ToDo toDo2 = makeToDo("toDo6");

        //make ref & check response
        ResponseEntity<String> response = getResponse(String.format("/api/todos/%d/addref", toDo1.getId()), toDo2.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        //make ref double = overlap (expected = 500 error)
        ResponseEntity<String> response2 = getResponse(String.format("/api/todos/%d/addref", toDo1.getId()), toDo2.getId());
        assertThat(response2.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        //check ref is same at first addref. & second request doesn't reflected.
        assertEquals(getOne(toDo1).getReferingToDos().getSize(), getOne(toDo2).getReferedToDos().getSize(), 1);     //size always 1.
    }

    @Test
    public void addRefTest_success_three_todos() {
        //create two todos
        ToDo toDo1 = makeToDo("toDo7");
        ToDo toDo2 = makeToDo("toDo8");
        ToDo toDo3 = makeToDo("toDo9");

        ResponseEntity<String> response = getResponse(String.format("/api/todos/%d/addref", toDo1.getId()), toDo2.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response2 = getResponse(String.format("/api/todos/%d/addref", toDo1.getId()), toDo3.getId());
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //check ref is success
        assertEquals(getOne(toDo1).getReferingToDos().getSize(), 2);
        assertThat(getOne(toDo1).getReferingToDoTitle(0), is(toDo2.getTitle()));
        assertThat(getOne(toDo1).getReferingToDoTitle(1), is(toDo3.getTitle()));

        assertThat(getOne(toDo2).getReferedToDoTitle(0), is(toDo1.getTitle()));
        assertThat(getOne(toDo3).getReferedToDoTitle(0), is(toDo1.getTitle()));
    }

    @Test
    public void updateTitleName_success() throws InterruptedException {
        //create two todos
        ToDo toDo = makeToDo("toDo10");
        String createDate = toDo.getFormattedCreatedDate();

        Thread.sleep(1000);
        template.put(String.format("/api/todos/%d", toDo.getId()), "test_changed",  String.class);
        String modifiedDate = getOne(toDo).getFormattedModifiedDate();

        assertEquals(getOne(toDo).getTitle(), "test_changed");
        assertNotEquals(createDate, modifiedDate);
    }

    @Test
    public void deleteRefTest_success() {
        //create two todos
        ToDo toDo1 = makeToDo("toDo11");
        ToDo toDo2 = makeToDo("toDo12");
        //addref
        ResponseEntity<String> response = getResponse(String.format("/api/todos/%d/addref", toDo1.getId()), toDo2.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        log.debug("~~" + getOne(toDo2).getReferedToDos().toString());
        assertEquals(getOne(toDo1).getReferingToDos().getSize(), 1);
        assertEquals(getOne(toDo2).getReferedToDos().getSize(), 1);

        template.delete(String.format("/api/todos/%d/%d", toDo1.getId(), toDo2.getId()));

        assertEquals(getOne(toDo1).getReferingToDos().getSize(), 0);
        assertEquals(getOne(toDo2).getReferedToDos().getSize(), 0);
    }

    @Test
    public void completeToDoTest_success() {
        //create two todos
        ToDo toDo1 = makeToDo("toDo13");
        ToDo toDo2 = makeToDo("toDo14");
        //addref
        ResponseEntity<String> response = getResponse(String.format("/api/todos/%d/addref", toDo1.getId()), toDo2.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        ResponseEntity<String> response2 = getResponse(String.format("/api/todos/%d/done", toDo1.getId()), null);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        ResponseEntity<String> response3 = getResponse(String.format("/api/todos/%d/done", toDo2.getId()), null);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        assertTrue(getOne(toDo1).isDone());
        assertTrue(getOne(toDo2).isDone());
    }

    @Test
    public void completeToDoTest_fail_referedToDo_not_complete() {
        //create two todos
        ToDo toDo1 = makeToDo("toDo15");
        ToDo toDo2 = makeToDo("toDo16");
        //addref
        ResponseEntity<String> response = getResponse(String.format("/api/todos/%d/addref", toDo1.getId()), toDo2.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        ResponseEntity<String> response2 = getResponse(String.format("/api/todos/%d/done", toDo2.getId()), null);
        assertThat(response2.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        ResponseEntity<String> response3 = getResponse(String.format("/api/todos/%d/done", toDo1.getId()), null);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        assertTrue(getOne(toDo1).isDone());
        assertFalse(getOne(toDo2).isDone());
    }

    @Test
    public void addrefTest_fail_bidirectional_chain_problem() {
        //create two todos
        ToDo toDo1 = makeToDo("toDo17");
        ToDo toDo2 = makeToDo("toDo18");
        ToDo toDo3 = makeToDo("toDo19");
        ToDo toDo4 = makeToDo("toDo20");

        ResponseEntity<String> response = getResponse(String.format("/api/todos/%d/addref", toDo1.getId()), toDo2.getId());
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response2 = getResponse(String.format("/api/todos/%d/addref", toDo2.getId()), toDo3.getId());
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response3 = getResponse(String.format("/api/todos/%d/addref", toDo3.getId()), toDo4.getId());
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response4 = getResponse(String.format("/api/todos/%d/addref", toDo4.getId()), toDo1.getId());
        assertThat(response4.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    public void test() {
        ToDo toDo1 = makeToDo("toDo21");
        ToDo toDo2= makeToDo("toDo21");
//        while (true) {
//
//        }
        ToDo toDo3 = makeToDo("toDo22");
        log.error("에러 : " + toDo1.getId());
        log.error("에러 : " + toDo3.getId());
        Long expected = toDo1.getId();
        Long actual = toDo3.getId();

        assertThat(expected + 1, is(actual));
    }
}
