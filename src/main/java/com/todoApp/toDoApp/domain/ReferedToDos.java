package com.todoApp.toDoApp.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Embeddable
public class ReferedToDos {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ToDo> referedToDos = new ArrayList<>();

    public ReferedToDos addReferedToDo(ToDo toDo) {
        this.referedToDos.add(toDo);
        return this;
    }

    public void deleteReferedToDo(ToDo toDo) {
        Iterator<ToDo> iterator = this.referedToDos.iterator();
        while (iterator.hasNext()) {
            ToDo referedToDo = iterator.next();

            if (referedToDo.equals(toDo)) {
                iterator.remove();
            }
        }
    }

    public int getSize() {
        return this.referedToDos.size();
    }

    public ToDo getToDo(int index) {
        return referedToDos.get(index);
    }

    public boolean isAlreadyReferedFrom(ToDo toDo) {
        for (ToDo referedToDo : referedToDos) {
            if (referedToDo.equals(toDo)) {
                return true;
            }
        }
        return false;
    }

    public boolean isReadyState() {
        if (referedToDos.size() == 0) {
            return true;
        }
        for (ToDo referedToDo : referedToDos) {
            if (!referedToDo.isDone()) {
                return false;
            }
        }
        return true;
    }

    public boolean isAllReferedToDoDone() {
        if (referedToDos.isEmpty()) {
            return true;
        }
        for (ToDo referedToDo : referedToDos) {
            if (!referedToDo.isDone()) {
                return false;
            }
        }
        return true;
    }

    public String getValueOf(int index) throws IndexOutOfBoundsException {
        return referedToDos.get(index).getTitle();
    }

    @Override
    public String toString() {
        return "ReferedToDos{" +
                "referedToDos=" + referedToDos +
                '}';
    }
}
