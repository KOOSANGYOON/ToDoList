package com.todoApp.toDoApp.service;

import com.todoApp.toDoApp.domain.ToDo;
import com.todoApp.toDoApp.domain.ToDoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ToDoService {
    private static final Logger log = LoggerFactory.getLogger(ToDoService.class);

    @Autowired
    private ToDoRepository toDoRepository;

    public List<Integer> calculatePage() {
        List<Integer> pageNumList = new ArrayList<>();
        int contentNum = 5;
        int pageNum;
        pageNum = toDoRepository.findAll().size() / contentNum;
        if (toDoRepository.findAll().size() % contentNum != 0) {
            pageNum++;
        }
        for (int i = 0; i < pageNum; i++) {
            pageNumList.add(i);
        }
        return pageNumList;
    }

    public ToDo addToDo(String newTitle) throws DataIntegrityViolationException {
        try {
            if (toDoRepository.findByTitle(newTitle) != null) {
                throw new DataIntegrityViolationException("not unique title.");
            }
            ToDo newToDo = new ToDo(newTitle);      //title중복시 exception 발생.
            return toDoRepository.save(newToDo);
        }catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            throw new DataIntegrityViolationException("data name error occur.");
        }
    }

    public ToDo addRef(Long id, Long referingId) throws Exception, IllegalArgumentException{
        ToDo toDo = toDoRepository.findOne(id);
        ToDo targetToDo = toDoRepository.findOne(referingId);

        toDo = toDo.registerReferingToDo(targetToDo);
        targetToDo = targetToDo.registerReferedToDo(toDo);

        return toDo;
    }

    public ToDo updateToDo(Long id, String title) {
        ToDo targetToDo = toDoRepository.findOne(id);
        targetToDo.updateTitle(title);
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

