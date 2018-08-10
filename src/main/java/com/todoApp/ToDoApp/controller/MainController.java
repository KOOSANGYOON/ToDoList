package com.todoApp.ToDoApp.controller;

import com.todoApp.ToDoApp.domain.User;
import com.todoApp.ToDoApp.service.ToDoService;
import com.todoApp.ToDoApp.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "toDoService")
    private ToDoService toDoService;

    @GetMapping("")
    public String main(Model model, HttpSession httpSession) {
        try{
            User loginUser = (User) httpSession.getAttribute("loginedUser");
        }catch (NullPointerException e) {
            log.error("로그인이 되지 않은 상태입니다. -> 로그인 창으로 이동합니다.");
            e.printStackTrace();
            return "redirect:/login";
        }

        return "index";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String user_id, String passwd, HttpSession httpSession, Model model) {
        try {
            User loginedUser = userService.login(user_id, passwd);
            httpSession.setAttribute("loginedUser", loginedUser);
        }catch (AuthenticationException e) {
            log.error("ERROR!! : 로그인 오류.");
            e.printStackTrace();
            return "redirect:/login";
        }
        return "redirect:/";
    }
}
