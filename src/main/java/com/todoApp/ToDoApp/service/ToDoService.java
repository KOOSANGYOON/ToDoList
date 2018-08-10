package com.todoApp.ToDoApp.service;

import com.todoApp.ToDoApp.domain.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ToDoService {
    @Autowired
    private ToDoRepository toDoRepository;

//    public Model findAllToDo(Model model) {
//        model.addAllAttributes()
//    }
}
