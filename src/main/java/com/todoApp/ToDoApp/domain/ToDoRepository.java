package com.todoApp.ToDoApp.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    ToDo findByTitle(String title);
}
