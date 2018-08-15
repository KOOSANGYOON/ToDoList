'use strict';

function addToDo(e) {
    var newTitle = $("#add-todo-textarea").val();
    var url = $(".addToDo").val();

    console.log("title", newTitle);
    console.log("url", url);

    $.ajax({
        type: 'post',
        url: url,
        contentType: 'text/html; charset=utf-8',
        data: newTitle,
        dataType: 'json'}).done(function createToDoSuccess(data) {
            console.log("success");
            window.location.reload();
    }).fail(function createToDoFail() {
            console.log("fail");
            alert("이름이 중복됩니다.");
    });
}

function addref(e) {
    var targetToDo = $("#targetToDo option:selected").text();
    var toDoId = $(".addrefBtn").val();

    // var targetToDo = $("#targetToDo").val();
    console.log(targetToDo);
    console.log(toDoId);
}