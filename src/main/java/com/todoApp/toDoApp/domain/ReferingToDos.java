package com.todoApp.toDoApp.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Embeddable
public class ReferingToDos {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ToDo_between_referingToDos",
        joinColumns = @JoinColumn(name = "ToDoId"),
        inverseJoinColumns = @JoinColumn(name = "referingToDoId"))
    private List<ToDo> referingToDos = new ArrayList<>();

    public ReferingToDos addReferingToDo(ToDo toDo) {
        this.referingToDos.add(toDo);
        return this;
    }

    public void deleteReferingToDo(ToDo toDo) {
        Iterator<ToDo> iterator = this.referingToDos.iterator();
        while (iterator.hasNext()) {
            ToDo referingToDo = iterator.next();

            if (referingToDo.equals(toDo)) {
                iterator.remove();
            }
        }
    }

    public ToDo getToDo(int index) {
        return referingToDos.get(index);
    }

    public int getSize() {
        return this.referingToDos.size();
    }

    public boolean isAlreadyRefering(ToDo toDo) {
        for (ToDo referingToDo : referingToDos) {
            if (referingToDo.equals(toDo)) {
                return true;
            }
        }
        return false;
    }

    public String getValueOf(int index) throws IndexOutOfBoundsException {
        return referingToDos.get(index).getTitle();
    }

    @Override
    public String toString() {
        return "ReferingToDos{" +
                "referingToDos=" + referingToDos +
                '}';
    }
}
