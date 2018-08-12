package com.todoApp.ToDoApp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class User extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = false, nullable = false)
    private String userId;

    @Column(nullable = false)
    @JsonIgnore
    private String passwd;

    @Column(nullable = false)
    private String userName;

    public User() {}

    public User(String userId, String passwd, String userName) {
        this.userId = userId;
        this.passwd = passwd;
        this.userName = userName;
    }

    public boolean matchPassword(String passwd) {
        return this.passwd.equals(passwd);
    }

    //getter
    public String getUserId() {
        return userId;
    }
    public String getPasswd() {
        return passwd;
    }
    public String getUserName() {
        return userName;
    }

    //equals / hashcode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(userId, user.userId) &&
                Objects.equals(passwd, user.passwd) &&
                Objects.equals(userName, user.userName);
    }
    @Override
    public int hashCode() {

        return Objects.hash(id, userId, passwd, userName);
    }
}
