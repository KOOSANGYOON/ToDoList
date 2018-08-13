package com.todoApp.ToDoApp.web;

import com.todoApp.ToDoApp.domain.ToDo;
import com.todoApp.ToDoApp.domain.ToDoRepository;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ToDoControllerTest {

    private static final Logger log = LoggerFactory.getLogger(ToDoControllerTest.class);

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private ToDoRepository toDoRepository;

    @Test
    public void createToDoTest_success() {
        String newTitle = "newToDo";
        assertNull(toDoRepository.findByTitle(newTitle));       //생성 전, null 인지 판단.
        ResponseEntity<String> response = template.postForEntity("/api/todos/create", newTitle,  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertNotNull(toDoRepository.findByTitle(newTitle));
    }

    @Test
    public void createToDoTest_fail_title_not_unique() {
        String newTitle = "todoFail";
        assertNull(toDoRepository.findByTitle(newTitle));       //생성 전, null 인지 판단.
        ResponseEntity<String> response = template.postForEntity("/api/todos/create", newTitle,  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertNotNull(toDoRepository.findByTitle(newTitle));
        ResponseEntity<String> response2 = template.postForEntity("/api/todos/create", newTitle,  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    public void addRefTest_success() {
        //create two todos
        ResponseEntity<String> response1 = template.postForEntity("/api/todos/create", "addRefToDo",  String.class);
        assertThat(response1.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response2 = template.postForEntity("/api/todos/create", "addRefToDo2",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //get todos
        ToDo todoOne = toDoRepository.findByTitle("addRefToDo");
        ToDo todoTwo = toDoRepository.findByTitle("addRefToDo2");
        String url = "/api/todos/" + todoOne.getId() + "/addref";

        //make ref & check response
        ResponseEntity<String> response3 = template.postForEntity(url, todoTwo.getId(),  String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        //check ref is success
        assertEquals(toDoRepository.findOne(todoOne.getId()).getReferingToDos().size(), 1);
        assertThat(toDoRepository.findOne(todoOne.getId()).getReferingToDos().get(0).getTitle(), is(todoTwo.getTitle()));   //title == unique
        assertEquals(toDoRepository.findOne(todoTwo.getId()).getReferedToDos().size(), 1);
        assertThat(toDoRepository.findOne(todoTwo.getId()).getReferedToDos().get(0).getTitle(), is(todoOne.getTitle()));   //title == unique
    }

    @Test
    public void addRefTest_fail_bi_direction() {
        //create two todos
        ResponseEntity<String> response1 = template.postForEntity("/api/todos/create", "failAddRef",  String.class);
        assertThat(response1.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response2 = template.postForEntity("/api/todos/create", "failAddRef2",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //get todos
        ToDo todoOne = toDoRepository.findByTitle("failAddRef");
        ToDo todoTwo = toDoRepository.findByTitle("failAddRef2");
        String url = "/api/todos/" + todoOne.getId() + "/addref";

        //make ref & check response
        ResponseEntity<String> response3 = template.postForEntity(url, todoTwo.getId(),  String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        //make bi-directional ref (expected = 500 error)
        String url2 = "/api/todos/" + todoTwo.getId() + "/addref";
        ResponseEntity<String> response4 = template.postForEntity(url2, todoOne.getId(),  String.class);
        assertThat(response4.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        //check ref is same at first addref. & second request doesn't reflected.
        assertEquals(toDoRepository.findOne(todoOne.getId()).getReferingToDos().size(), 1);     //size always 1.
        assertThat(toDoRepository.findOne(todoOne.getId()).getReferingToDos().get(0).getTitle(), is(todoTwo.getTitle()));   //title == unique
        assertEquals(toDoRepository.findOne(todoTwo.getId()).getReferedToDos().size(), 1);      //size always 1.
        assertThat(toDoRepository.findOne(todoTwo.getId()).getReferedToDos().get(0).getTitle(), is(todoOne.getTitle()));   //title == unique
    }

    @Test
    public void addRefTest_fail_addref_double() {
        //create two todos
        ResponseEntity<String> response1 = template.postForEntity("/api/todos/create", "addDouble",  String.class);
        assertThat(response1.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response2 = template.postForEntity("/api/todos/create", "addDouble2",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //get todos
        ToDo todoOne = toDoRepository.findByTitle("addDouble");
        ToDo todoTwo = toDoRepository.findByTitle("addDouble2");
        String url = "/api/todos/" + todoOne.getId() + "/addref";

        //make ref & check response
        ResponseEntity<String> response3 = template.postForEntity(url, todoTwo.getId(),  String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        //make ref double = overlap (expected = 500 error)
        ResponseEntity<String> response4 = template.postForEntity(url, todoTwo.getId(),  String.class);
        assertThat(response4.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));

        //check ref is same at first addref. & second request doesn't reflected.
        assertEquals(toDoRepository.findOne(todoOne.getId()).getReferingToDos().size(), 1);     //size always 1.
        assertThat(toDoRepository.findOne(todoOne.getId()).getReferingToDos().get(0).getTitle(), is(todoTwo.getTitle()));   //title == unique
        assertEquals(toDoRepository.findOne(todoTwo.getId()).getReferedToDos().size(), 1);      //size always 1.
        assertThat(toDoRepository.findOne(todoTwo.getId()).getReferedToDos().get(0).getTitle(), is(todoOne.getTitle()));   //title == unique
    }

    @Test
    @Transactional
    public void addRefTest_success_three_todos() {
        //create two todos
        ResponseEntity<String> response1 = template.postForEntity("/api/todos/create", "successToDo",  String.class);
        assertThat(response1.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response2 = template.postForEntity("/api/todos/create", "successToDo2",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response3 = template.postForEntity("/api/todos/create", "successToDo3",  String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        //get todos
        ToDo todoOne = toDoRepository.findByTitle("successToDo");
        ToDo todoTwo = toDoRepository.findByTitle("successToDo2");
        ToDo todoThree = toDoRepository.findByTitle("successToDo3");
        String url = "/api/todos/" + todoOne.getId() + "/addref";

        //make ref & check response
        ResponseEntity<String> response4 = template.postForEntity(url, todoTwo.getId(),  String.class);
        assertThat(response4.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response5 = template.postForEntity(url, todoThree.getId(),  String.class);
        assertThat(response5.getStatusCode(), is(HttpStatus.OK));

        todoOne = toDoRepository.findOne(todoOne.getId());
        todoTwo = toDoRepository.findOne(todoTwo.getId());
        todoThree = toDoRepository.findOne(todoThree.getId());
        System.out.println("~todoOne | refering : " + todoOne.getReferingToDos() + " | refered : " + todoOne.getReferedToDos());
        System.out.println("~todoTwo | refering : " + todoTwo.getReferingToDos() + " | refered : " + todoTwo.getReferedToDos());
        System.out.println("~todoThree | refering : " + todoThree.getReferingToDos() + " | refered : " + todoThree.getReferedToDos());

        //check ref is success
        assertEquals(toDoRepository.findOne(todoOne.getId()).getReferingToDos().size(), 2);     //size is 2.
        assertThat(toDoRepository.findOne(todoOne.getId()).getReferingToDos().get(0).getTitle(), is(todoTwo.getTitle()));   //title == unique
        assertThat(toDoRepository.findOne(todoOne.getId()).getReferingToDos().get(1).getTitle(), is(todoThree.getTitle()));   //title == unique

        assertEquals(toDoRepository.findOne(todoTwo.getId()).getReferedToDos().size(), 1);
        assertThat(toDoRepository.findOne(todoTwo.getId()).getReferedToDos().get(0).getTitle(), is(todoOne.getTitle()));   //title == unique

        assertEquals(toDoRepository.findOne(todoThree.getId()).getReferedToDos().size(), 1);
        assertThat(toDoRepository.findOne(todoThree.getId()).getReferedToDos().get(0).getTitle(), is(todoOne.getTitle()));   //title == unique
    }

    @Test
    public void updateTitleName_success() {
        ResponseEntity<String> response = template.postForEntity("/api/todos/create", "test1",  String.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));

        assertNull(toDoRepository.findByTitle("test_changed"));

        ToDo targetToDo = toDoRepository.findByTitle("test1");
        Long savedGetId = targetToDo.getId();

        template.put(String.format("/api/todos/%d", targetToDo.getId()), "test_changed",  String.class);

        assertNull(toDoRepository.findByTitle("test1"));
        assertNotNull(toDoRepository.findByTitle("test_changed"));
        assertEquals(toDoRepository.findByTitle("test_changed").getId(), savedGetId);
    }

    @Test
    public void deleteRefTest_success() {
        //create two todos
        ResponseEntity<String> response1 = template.postForEntity("/api/todos/create", "test_1",  String.class);
        assertThat(response1.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response2 = template.postForEntity("/api/todos/create", "test_2",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //get todos
        ToDo todoOne = toDoRepository.findByTitle("test_1");
        ToDo todoTwo = toDoRepository.findByTitle("test_2");
        String url = "/api/todos/" + todoOne.getId() + "/addref";

        //make ref & check response
        ResponseEntity<String> response3 = template.postForEntity(url, todoTwo.getId(),  String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        assertEquals(toDoRepository.findByTitle("test_1").getReferingToDos().size(), 1);
        assertEquals(toDoRepository.findByTitle("test_2").getReferedToDos().size(), 1);

        template.delete(String.format("/api/todos/%d/%d", todoOne.getId(), todoTwo.getId()));

        assertEquals(toDoRepository.findByTitle("test_1").getReferingToDos().size(), 0);
        assertEquals(toDoRepository.findByTitle("test_2").getReferedToDos().size(), 0);
    }

    @Test
    public void completeToDoTest_success() {
        //create two todos
        ResponseEntity<String> response1 = template.postForEntity("/api/todos/create", "complete_test",  String.class);
        assertThat(response1.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response2 = template.postForEntity("/api/todos/create", "complete_test2",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //get todos / make url
        ToDo todoOne = toDoRepository.findByTitle("complete_test");
        ToDo todoTwo = toDoRepository.findByTitle("complete_test2");
        String url = "/api/todos/" + todoOne.getId() + "/addref";

        ResponseEntity<String> response3 = template.postForEntity(url, todoTwo.getId(),  String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        ResponseEntity<String> response4 = template.postForEntity(String.format("/api/todos/%d/done", todoOne.getId()), null, String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response5 = template.postForEntity(String.format("/api/todos/%d/done", todoTwo.getId()), null, String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        System.out.println("test One : " + toDoRepository.findByTitle("complete_test").isDone());
        System.out.println("test Two : " + toDoRepository.findByTitle("complete_test2").isDone());

        assertTrue(toDoRepository.findByTitle("complete_test").isDone());
        assertTrue(toDoRepository.findByTitle("complete_test2").isDone());
    }

    @Test       //need to update
    public void completeToDoTest_fail_referedToDo_not_complete() {
        //create two todos
        ResponseEntity<String> response1 = template.postForEntity("/api/todos/create", "complete",  String.class);
        assertThat(response1.getStatusCode(), is(HttpStatus.OK));
        ResponseEntity<String> response2 = template.postForEntity("/api/todos/create", "complete2",  String.class);
        assertThat(response2.getStatusCode(), is(HttpStatus.OK));

        //get todos / make url
        ToDo todoOne = toDoRepository.findByTitle("complete");
        ToDo todoTwo = toDoRepository.findByTitle("complete2");
        String url = "/api/todos/" + todoOne.getId() + "/addref";

        ResponseEntity<String> response3 = template.postForEntity(url, todoTwo.getId(),  String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        ResponseEntity<String> response4 = template.postForEntity(String.format("/api/todos/%d/done", todoTwo.getId()), null, String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        ResponseEntity<String> response5 = template.postForEntity(String.format("/api/todos/%d/done", todoOne.getId()), null, String.class);
        assertThat(response3.getStatusCode(), is(HttpStatus.OK));

        assertTrue(toDoRepository.findByTitle("complete_test").isDone());
        assertFalse(toDoRepository.findByTitle("complete_test2").isDone());
    }


}
