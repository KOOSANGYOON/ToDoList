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
import org.springframework.transaction.annotation.Transactional;

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

//        while(true) {
//
//        }

        //check ref is success
        assertEquals(getOne(toDo1).getReferingToDos().getSize(), 2);
        assertThat(getOne(toDo1).getReferingToDoTitle(0), is(toDo2.getTitle()));
        assertThat(getOne(toDo1).getReferingToDoTitle(1), is(toDo3.getTitle()));

        log.debug("~~" + getOne(toDo1).getReferedToDos().toString());
        log.debug("~~" + getOne(toDo2).getReferedToDos().toString());
        log.debug("~~" + getOne(toDo3).getReferedToDos().toString());

        log.debug("~~" + getOne(toDo1).getReferingToDos().toString());
        log.debug("~~" + getOne(toDo2).getReferingToDos().toString());
        log.debug("~~" + getOne(toDo3).getReferingToDos().toString());

        assertEquals(getOne(toDo2).getReferedToDos().getSize(), 1);
        assertEquals(getOne(toDo3).getReferedToDos().getSize(), 1);
        assertThat(getOne(toDo2).getReferedToDoTitle(0), is(toDo1.getTitle()));
        assertThat(getOne(toDo3).getReferedToDoTitle(0), is(toDo1.getTitle()));
    }

//    @Test
//    public void updateTitleName_success() {
//        ResponseEntity<String> response = template.postForEntity("/api/todos/create", "test1",  String.class);
//        assertThat(response.getStatusCode(), is(HttpStatus.OK));
//
//        assertNull(toDoRepository.findByTitle("test_changed"));
//
//        ToDo targetToDo = toDoRepository.findByTitle("test1");
//        Long savedGetId = targetToDo.getId();
//
//        template.put(String.format("/api/todos/%d", targetToDo.getId()), "test_changed",  String.class);
//
//        assertNull(toDoRepository.findByTitle("test1"));
//        assertNotNull(toDoRepository.findByTitle("test_changed"));
//        assertEquals(toDoRepository.findByTitle("test_changed").getId(), savedGetId);
//    }
//
//    @Test
//    public void deleteRefTest_success() {
//        //create two todos
//        ResponseEntity<String> response1 = template.postForEntity("/api/todos/create", "test_1",  String.class);
//        assertThat(response1.getStatusCode(), is(HttpStatus.OK));
//        ResponseEntity<String> response2 = template.postForEntity("/api/todos/create", "test_2",  String.class);
//        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
//
//        //get todos
//        ToDo todoOne = toDoRepository.findByTitle("test_1");
//        ToDo todoTwo = toDoRepository.findByTitle("test_2");
//        String url = "/api/todos/" + todoOne.getId() + "/addref";
//
//        //make ref & check response
//        ResponseEntity<String> response3 = template.postForEntity(url, todoTwo.getId(),  String.class);
//        assertThat(response3.getStatusCode(), is(HttpStatus.OK));
//
//        assertEquals(toDoRepository.findByTitle("test_1").getReferingToDos().getSize(), 1);
//        assertEquals(toDoRepository.findByTitle("test_2").getReferedToDos().getSize(), 1);
//
//        template.delete(String.format("/api/todos/%d/%d", todoOne.getId(), todoTwo.getId()));
//
//        assertEquals(toDoRepository.findByTitle("test_1").getReferingToDos().getSize(), 0);
//        assertEquals(toDoRepository.findByTitle("test_2").getReferedToDos().getSize(), 0);
//    }
//
//    @Test
//    public void completeToDoTest_success() {
//        //create two todos
//        ResponseEntity<String> response1 = template.postForEntity("/api/todos/create", "complete_test",  String.class);
//        assertThat(response1.getStatusCode(), is(HttpStatus.OK));
//        ResponseEntity<String> response2 = template.postForEntity("/api/todos/create", "complete_test2",  String.class);
//        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
//
//        //get todos / make url
//        ToDo todoOne = toDoRepository.findByTitle("complete_test");
//        ToDo todoTwo = toDoRepository.findByTitle("complete_test2");
//        String url = "/api/todos/" + todoOne.getId() + "/addref";
//
//        ResponseEntity<String> response3 = template.postForEntity(url, todoTwo.getId(),  String.class);
//        assertThat(response3.getStatusCode(), is(HttpStatus.OK));
//
//        ResponseEntity<String> response4 = template.postForEntity(String.format("/api/todos/%d/done", todoOne.getId()), null, String.class);
//        assertThat(response3.getStatusCode(), is(HttpStatus.OK));
//        ResponseEntity<String> response5 = template.postForEntity(String.format("/api/todos/%d/done", todoTwo.getId()), null, String.class);
//        assertThat(response3.getStatusCode(), is(HttpStatus.OK));
//
//        System.out.println("test One : " + toDoRepository.findByTitle("complete_test").isDone());
//        System.out.println("test Two : " + toDoRepository.findByTitle("complete_test2").isDone());
//
//        assertTrue(toDoRepository.findByTitle("complete_test").isDone());
//        assertTrue(toDoRepository.findByTitle("complete_test2").isDone());
//    }
//
//    @Test       //need to update
//    public void completeToDoTest_fail_referedToDo_not_complete() {
//        //create two todos
//        ResponseEntity<String> response1 = template.postForEntity("/api/todos/create", "complete",  String.class);
//        assertThat(response1.getStatusCode(), is(HttpStatus.OK));
//        ResponseEntity<String> response2 = template.postForEntity("/api/todos/create", "complete2",  String.class);
//        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
//
//        //get todos / make url
//        ToDo todoOne = toDoRepository.findByTitle("complete");
//        ToDo todoTwo = toDoRepository.findByTitle("complete2");
//        String url = "/api/todos/" + todoOne.getId() + "/addref";
//
//        ResponseEntity<String> response3 = template.postForEntity(url, todoTwo.getId(),  String.class);
//        assertThat(response3.getStatusCode(), is(HttpStatus.OK));
//
//        ResponseEntity<String> response4 = template.postForEntity(String.format("/api/todos/%d/done", todoTwo.getId()), null, String.class);
//        assertThat(response3.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
//        ResponseEntity<String> response5 = template.postForEntity(String.format("/api/todos/%d/done", todoOne.getId()), null, String.class);
//        assertThat(response3.getStatusCode(), is(HttpStatus.OK));
//
//        assertTrue(toDoRepository.findByTitle("complete_test").isDone());
//        assertFalse(toDoRepository.findByTitle("complete_test2").isDone());
//    }


}
