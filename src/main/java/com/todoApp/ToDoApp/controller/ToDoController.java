package com.todoApp.ToDoApp.controller;

import com.todoApp.ToDoApp.domain.ToDo;
import com.todoApp.ToDoApp.service.ToDoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/todos")
public class ToDoController {
    private static final Logger log = LoggerFactory.getLogger(ToDoController.class);

    @Resource(name = "toDoService")
    private ToDoService toDoService;

    @PostMapping("/create")
    public ToDo createToDo(@RequestBody String newTitle) {
        ToDo newToDo = null;
        try {
            newToDo =  toDoService.addToDo(newTitle);
        }catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new DataIntegrityViolationException("this title is not unique.");
        }
        return newToDo;
    }

    @PostMapping("/{id}/addref")
    public ToDo addRef(@PathVariable Long id, @RequestBody Long referingId) throws Exception {
        try {
            return toDoService.addRef(id, referingId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("bi-direction occurs.");
        }
    }

    @PutMapping("/{id}")
    public ToDo updateToDo(@PathVariable Long id, @RequestBody String title) {
        return toDoService.updateToDo(id, title);
    }

    @DeleteMapping("/{todo_id}/{target_id}")
    public ToDo deleteRef(@PathVariable Long todo_id, @PathVariable Long target_id) {
        return toDoService.deleteRef(todo_id, target_id);
    }

    @PostMapping("/{id}/done")
    public ToDo completeToDo(@PathVariable Long id) throws Exception {
        try {
            return toDoService.completeToDo(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("not ready to done.");
        }
    }
}
