package com.todoApp.ToDoApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class ToDo extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private boolean isDone = false;

    private boolean readyState = true;

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

    //registered / registering
    public void registerReferedToDo(ToDo toDo) {
        this.referedToDos.add(toDo);
        if (!isAllReferedToDoDone()) {
            readyState = false;
        }
    }
    public void registerReferingToDo(ToDo toDo) throws Exception {
        if (isAlreadyReferedFrom(toDo)) {
            throw new Exception("This ToDo is Already registered.");
        }
        this.referingToDos.add(toDo);
    }

    public void complete() throws Exception {
        if (!isAllReferedToDoDone()) {
            throw new Exception("Not ready to done.");
        }
        this.isDone = true;
    }

    private boolean isAllReferedToDoDone() {
        if (referedToDos.isEmpty()) {
            return true;
        }
        for (ToDo toDo : referedToDos) {
            if (!toDo.isDone) {
                return false;
            }
        }
        return true;
    }

    //prevent bi-direction
    private boolean isAlreadyReferedFrom(ToDo toDo) {
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
    public boolean isReadyState() {
        return isAllReferedToDoDone();
    }
    public List<ToDo> getReferedToDos() {
        return referedToDos;
    }
    public List<ToDo> getReferingToDos() {
        return referingToDos;
    }

    //equals / hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ToDo)) return false;
        ToDo toDo = (ToDo) o;
        return isDone == toDo.isDone &&
                readyState == toDo.readyState &&
                Objects.equals(id, toDo.id) &&
                Objects.equals(title, toDo.title) &&
                Objects.equals(referedToDos, toDo.referedToDos) &&
                Objects.equals(referingToDos, toDo.referingToDos);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, title, isDone, readyState, referedToDos, referingToDos);
    }
}
