package com.todoApp.toDoApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    private boolean readyState = true;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "referedToDos"))
    @JsonIgnore
    private List<ToDo> referedToDos = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(foreignKey = @ForeignKey(name = "referingToDos"))
    @JsonIgnore
    private List<ToDo> referingToDos = new ArrayList<>();

    public ToDo() {}
    public ToDo(String title) {
        this.title = title;
    }

    //registered / registering
    public ToDo registerReferedToDo(ToDo toDo) {
        this.referedToDos.add(toDo);
        if (!isAllReferedToDoDone()) {
            readyState = false;
        }
        return this;
    }
    public ToDo registerReferingToDo(ToDo toDo) throws Exception {
        if (isAlreadyRefering(toDo)) {
            throw new Exception("This ToDo is Already exist.");
        }
        if (isAlreadyReferedFrom(toDo)) {
            throw new Exception("bi-direction occur.");
        }
        this.referingToDos.add(toDo);
        return this;
    }

    //delete refered / refering todo
    public ToDo deleteReferedToDo(ToDo toDo) {
        Iterator<ToDo> iterator = this.referedToDos.iterator();
        while (iterator.hasNext()) {
            ToDo referedToDo = iterator.next();

            if (referedToDo.equals(toDo)) {
                iterator.remove();
            }
        }
        if (!isAllReferedToDoDone()) {
            readyState = false;
        }
        return this;
    }
    public ToDo deleteReferingToDo(ToDo toDo) {
        Iterator<ToDo> iterator = this.referingToDos.iterator();
        while (iterator.hasNext()) {
            ToDo referingToDo = iterator.next();

            if (referingToDo.equals(toDo)) {
                iterator.remove();
            }
        }
        return this;
    }

    private int findIndex(ArrayList<ToDo> list, ToDo toDo) throws NoSuchElementException {

        throw new NoSuchElementException("there are no element.");
    }

    public ToDo complete() throws Exception {
        if (!isAllReferedToDoDone()) {
            this.readyState = false;
            throw new Exception("Not ready to done.");
        }
//        if (!readyState) {
//            throw new Exception("Not ready to done.");
//        }
        this.isDone = true;
        return this;

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

    private boolean isAlreadyRefering(ToDo toDo) {
        for (ToDo referingToDo : this.referingToDos) {
            if (referingToDo.equals(toDo)) {
                return true;
            }
        }
        return false;
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
        return isAllReferedToDoDone();
    }
    public List<ToDo> getReferedToDos() {
        return referedToDos;
    }
    public List<ToDo> getReferingToDos() {
        return referingToDos;
    }

    //setter
    public void setTitle(String title) {
        this.title = title;
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
    //toString
    @Override
    public String toString() {
        return "ToDo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isDone=" + isDone +
                ", readyState=" + readyState +
                ", referedToDos=" + referedToDos +
                '}';
    }
}
