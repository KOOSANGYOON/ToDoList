package com.todoApp.ToDoApp.controller;

import com.todoApp.ToDoApp.AcceptanceTest;
import com.todoApp.ToDoApp.HtmlFormDataBuilder;
import com.todoApp.ToDoApp.domain.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class UserAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    @Autowired
    protected TestRestTemplate template;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void mainPageTest() {
        ResponseEntity<String> response = template.getForEntity("/", String.class);
        assertThat(response.getStatusCode(), CoreMatchers.is(HttpStatus.OK));
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void loginTest_login_success() {
        String userId = "koo";

        assertNotNull(userRepository.findByUserId(userId));

        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("user_id", userId)
                .addParameter("passwd", "test").build();

        ResponseEntity<String> response = template.postForEntity("/login", request, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertThat(response.getHeaders().getLocation().getPath(), is("/"));
    }

    @Test
    public void loginTest_login_fail_didnt_exist_id() {
        String userId = "hacker";       //there are no user that id is "hacker"

        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("user_id", userId)
                .addParameter("passwd", "test").build();

        ResponseEntity<String> response = template.postForEntity("/login", request, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertThat(response.getHeaders().getLocation().getPath(), is("/login"));
    }

    @Test
    public void loginTest_login_fail_passwd_not_correct() {
        String userId = "koo";

        HttpEntity<MultiValueMap<String, Object>> request = HtmlFormDataBuilder.urlEncodedForm()
                .addParameter("user_id", userId)
                .addParameter("passwd", "wrongPassword").build();

        ResponseEntity<String> response = template.postForEntity("/login", request, String.class);

        assertThat(response.getStatusCode(), is(HttpStatus.FOUND));
        assertThat(response.getHeaders().getLocation().getPath(), is("/login"));
    }
}
