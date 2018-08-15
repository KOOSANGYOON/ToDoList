package com.todoApp.toDoApp.controller;

import com.todoApp.toDoApp.domain.ToDo;
import com.todoApp.toDoApp.domain.ToDoRepository;
import com.todoApp.toDoApp.service.ToDoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

@Controller
public class HomeController {
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private ToDoRepository toDoRepository;

    @Resource(name = "toDoService")
    private ToDoService toDoService;

//    @GetMapping("")
//    public String main(Model model) {
//        model.addAttribute("todos", toDoRepository.findAll());
//        return "index";
//    }

    @GetMapping("")
    public String main(Model model, @PageableDefault(sort = { "id" }, direction = Sort.Direction.ASC, size = 5) Pageable pageable) {
        Page<ToDo> toDoPage = toDoRepository.findAll(pageable);
        model.addAttribute("todos", toDoPage);

        model.addAttribute("pages", toDoService.calculatePage());

        return "index";
    }

}
