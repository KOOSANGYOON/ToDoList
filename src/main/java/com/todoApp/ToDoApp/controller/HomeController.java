package com.todoApp.ToDoApp.controller;

import com.todoApp.ToDoApp.domain.ToDoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ToDoRepository toDoRepository;

    @GetMapping("")
    public String main(Model model) {
        model.addAttribute("todos", toDoRepository.findAll());
        return "index";
    }

}
