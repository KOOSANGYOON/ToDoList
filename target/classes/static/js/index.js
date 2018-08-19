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
    var toDoId = $(this).val();
    console.log("hahahaha", toDoId);

    var url = "/api/todos/" + toDoId + "/addref";
    console.log(url);

    var targetId = $(e.target).closest("#targetToDo").closest("#optionSelected option:selected").val();
    console.log(targetId);

    $.ajax({
        type: 'post',
        url: url,
        contentType: 'text/html; charset=utf-8',
        data: targetId,
        dataType: 'json'}).done(function createToDoSuccess(data) {
        console.log("success");
    }).fail(function createToDoFail() {
        console.log("fail");
        alert("참조를 걸 수 없습니다.");
    });
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
        $(e.target).closest(".content-body").css('background-color', '9FCD2C');
        $(e.target).closest("#checkBox").css('display', 'none');
        $(e.target).closest(".content-domain").css('background-color', 'black');
    }).fail(function createToDoFail() {
        console.log("fail");
        alert("선행 할일들을 먼저 마쳐주세요.");
    });
}