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
$(".checkBox").on("click", completeToDo);

function addref(e) {
    var test = $(e.target).closest("#targetToDo").val();
    // var test = $(e.target).closest("select").closest("option:selected").text();
    // var test = $(e.target).closest("#optionSelected option:selected").val();
    // var test = $(e.target).previousElementSibling;
    // var test = $(this).closest("select").attr("id");
    console.log("tt : ", test);
    var targetToDo = $("#targetToDo option:selected").text();
    var toDoId = $(".addrefBtn").val();

    // var targetToDo = $("#targetToDo").val();
    // console.log(targetToDo);
    console.log(toDoId);
}

function completeToDo(e) {
    console.log("hi");
    var toDoId = $(e.target).closest("input").val();
    console.log(toDoId);

    var url = "/api/todos/" + toDoId + "/done";
    console.log(url);

    $.ajax({
        type: 'post',
        url: url,
        contentType: 'text/html; charset=utf-8',
        data: toDoId,
        dataType: 'json'}).done(function createToDoSuccess(data) {
        console.log("success");
        // $(e.target).closest(".content-domain").style(backgroundColor)
        $(e.target).closest(".content-domain").css('background-color', '9FCD2C');
        $(e.target).closest(".checkBox").css('display', 'none');
        // window.location.reload();
    }).fail(function createToDoFail() {
        console.log("fail");
        alert("이름을 다시 설정해주세요.");
    });
}