package com.todoApp.ToDoApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ToDo extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private boolean isDone = false;

    private boolean readyStatus = true;

    @OneToMany
    @JsonIgnore
    private List<ToDo> referedToDos = new ArrayList<>();

    @OneToMany
    @JoinColumn(foreignKey = @ForeignKey(name = "referingToDos"))
    private List<ToDo> referingToDos = new ArrayList<>();

    public ToDo() {}
    public ToDo(String title) {
        this.title = title;
    }

    //register
    public void registerReferedToDo(ToDo toDo) {
        this.referedToDos.add(toDo);
    }
    public void registerReferingToDo(ToDo toDo) {
        this.referingToDos.add(toDo);
    }

    //prevent bi-direction
    public boolean isAlreadyRefered(ToDo toDo) {
        for (ToDo existToDo : this.referedToDos) {
            if (existToDo.equals(toDo)) {
                return true;
            }
        }
        return false;
    }

    //getter
    public String getTitle() {
        return title;
    }
    public boolean isDone() {
        return isDone;
    }
    public boolean isReadyStatus() {
        return readyStatus;
    }
    public List<ToDo> getReferedToDos() {
        return referedToDos;
    }
    public List<ToDo> getReferingToDos() {
        return referingToDos;
    }
}
