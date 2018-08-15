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
            alert("이름을 다시 설정해주세요.");
    });
}

// $(".addrefBtn").on("click", e => {
//     e.previousElementSibling.val();
// })

$(".addrefBtn").on("click", addref);

function addref(e) {
    var test = $(e.target).closest("option").attr("id");
    // var test = $(this).closest("select").attr("id");
    console.log("tt : ", test);
    var targetToDo = $("#targetToDo option:selected").text();
    var toDoId = $(".addrefBtn").val();

    // var targetToDo = $("#targetToDo").val();
    console.log(targetToDo);
    console.log(toDoId);
}