package com.todoApp.toDoApp.domain;

import javax.persistence.*;
import java.util.*;

@Entity
public class ToDo extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String title;

    private boolean isDone = false;

    @Embedded
    private ReferedToDos referedToDos = new ReferedToDos();

    @Embedded
    private ReferingToDos referingToDos = new ReferingToDos();

    public ToDo() {}
    public ToDo(String title) {
        this.title = title;
    }

    //registered / registering
    public ToDo registerReferedToDo(ToDo toDo) {
        if (!referedToDos.isAlreadyRefered(toDo)) {
            this.referedToDos.addReferedToDo(toDo);
        }
        updateModifiedDate();
        return this;
    }
    public ToDo registerReferingToDo(ToDo toDo) throws IllegalArgumentException {
        if (toDo.equals(this)) {
            throw new IllegalArgumentException("cannot register self.");
        }
        if (referingToDos.isAlreadyRefering(toDo)) {
            throw new IllegalArgumentException("This ToDo is Already exist.");
        }
        if (referedToDos.isAlreadyReferedFrom(toDo)) {
            throw new IllegalArgumentException("bi-direction occur.");
        }

        List<ToDo> copiedToDos = copyReferedToDos();
        toDo.referedToDos.preReferedBeforeRefering(copiedToDos);

        this.referingToDos.addReferingToDo(toDo);
        updateModifiedDate();
        return this;
    }

    public List<ToDo> copyReferedToDos() {
        List<ToDo> copiedToDos = referedToDos.copyReferedToDo();
        copiedToDos.add(this);
        return copiedToDos;
    }

    //delete refered / refering todo
    public ToDo deleteReferedToDo(ToDo toDo) {
        referedToDos.deleteReferedToDo(toDo);
        updateModifiedDate();
        return this;
    }
    public ToDo deleteReferingToDo(ToDo toDo) {
        referingToDos.deleteReferingToDo(toDo);
        updateModifiedDate();
        return this;
    }

    public ToDo complete() throws Exception {
        if (!referedToDos.isAllReferedToDoDone()) {
            throw new Exception("Not ready to done.");
        }
        this.isDone = true;
        updateModifiedDate();
        return this;
    }

    public void updateTitle(String title) {
        this.title = title;
        updateModifiedDate();
    }

    //getter
    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public boolean isDone() {
        return isDone;
    }
    public boolean isReadyState() {
        return referedToDos.isReadyState();
    }
    public ReferedToDos getReferedToDos() {
        return referedToDos;
    }
    public ReferingToDos getReferingToDos() {
        return referingToDos;
    }
    public String getReferingToDoTitle(int index) {
        return referingToDos.getValueOf(index);
    }
    public String getReferedToDoTitle(int index) {
        return referedToDos.getValueOf(index);
    }
    public String getReferId() {
        return referingToDos.getReferId();
    }

    //equals / hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ToDo)) return false;
        ToDo toDo = (ToDo) o;
        return isDone == toDo.isDone &&
                Objects.equals(id, toDo.id) &&
                Objects.equals(title, toDo.title) &&
                Objects.equals(referedToDos, toDo.referedToDos) &&
                Objects.equals(referingToDos, toDo.referingToDos);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, title, isDone, referedToDos, referingToDos);
    }
    //toString
    @Override
    public String toString() {
        return "ToDo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isDone=" + isDone +
                ", referedToDos=" + referedToDos +
                '}';
    }
}
