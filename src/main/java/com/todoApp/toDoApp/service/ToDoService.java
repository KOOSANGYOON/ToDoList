package com.todoApp.toDoApp.service;

import com.todoApp.toDoApp.domain.ToDo;
import com.todoApp.toDoApp.domain.ToDoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ToDoService {
    private static final Logger log = LoggerFactory.getLogger(ToDoService.class);

    @Autowired
    private ToDoRepository toDoRepository;

    public ToDo addToDo(String newTitle) throws DataIntegrityViolationException {
        ToDo newToDo = new ToDo(newTitle);      //title중복시 exception 발생.
        return toDoRepository.save(newToDo);
    }

    public ToDo addRef(Long id, Long referingId) throws Exception {
        ToDo toDo = toDoRepository.findOne(id);
        ToDo targetToDo = toDoRepository.findOne(referingId);

        toDo = toDo.registerReferingToDo(targetToDo);
        targetToDo = targetToDo.registerReferedToDo(toDo);

//        log.debug(toDo.getId() + "'s refering : " + toDo.getReferingToDos());
//        log.debug(toDo.getId() + "'s refered : " + toDo.getReferedToDos());
//        log.debug(targetToDo.getId() + "'s refering : " + targetToDo.getReferingToDos());
//        log.debug(targetToDo.getId() + "'s refered : " + targetToDo.getReferedToDos());

        return toDo;
    }

    public ToDo updateToDo(Long id, String title) {
        ToDo targetToDo = toDoRepository.findOne(id);
        targetToDo.setTitle(title);
        return targetToDo;
    }

    public ToDo deleteRef(Long id, Long referingId) {
        ToDo toDo = toDoRepository.findOne(id);
        ToDo targetToDo = toDoRepository.findOne(referingId);

        toDo = toDo.deleteReferingToDo(targetToDo);
        targetToDo = targetToDo.deleteReferedToDo(toDo);

        return toDo;
    }

    public ToDo completeToDo(Long id) throws Exception {
        ToDo toDo = toDoRepository.findOne(id);
        return toDo.complete();
    }

    public ToDo findOne(Long id) {
        return toDoRepository.findOne(id);
    }
}

