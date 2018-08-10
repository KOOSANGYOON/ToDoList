package com.todoApp.ToDoApp.service;

import com.todoApp.ToDoApp.domain.User;
import com.todoApp.ToDoApp.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.sasl.AuthenticationException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

//    public User login(String userId, String passwd) throws NullPointerException, AuthenticationException {
//        User findedUser = userRepository.findByUserId(userId).orElseThrow(NullPointerException::new);
//        if (!findedUser.matchPassword(passwd)) {
//            throw new AuthenticationException("password isn't correct.");
//        }
//        return findedUser;
//    }

    public User login(String userId, String passwd) throws AuthenticationException {
        User findedUser = userRepository.findByUserId(userId).orElseThrow(() -> new AuthenticationException("user id doesn't exist."));
        if (!findedUser.matchPassword(passwd)) {
            throw new AuthenticationException("password isn't correct.");
        }
        return findedUser;
    }
}
