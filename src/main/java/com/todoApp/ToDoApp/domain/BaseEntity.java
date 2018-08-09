package com.todoApp.ToDoApp.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    private LocalDateTime createdDate = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime modifiedDate = LocalDateTime.now();

    public String getFormattedCreatedDate(){
        return getFormattedDate(this.createdDate, "yyyy.MM.dd HH:mm:ss");
    }

    public String getFormattedModifiedDate(){
        return getFormattedDate(this.modifiedDate, "yyyy.MM.dd HH:mm:ss");
    }

    public void updateModifiedDate() { this.modifiedDate = LocalDateTime.now(); }

    private String getFormattedDate(LocalDateTime dateTime, String format){
        if (dateTime == null){
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }
}
