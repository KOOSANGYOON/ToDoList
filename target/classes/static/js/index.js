'use strict';

$(".addrefBtn").on("click", addref);
$(".complete").on("click", completeToDo);
$(".todoTitle").on("change", editTitle);

function addToDo() {
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

function addref(e) {
    var toDoId = $(this).val();
    console.log("hahahaha", toDoId);

    var box = document.getElementById('targetToDo' + toDoId);
    var targetId = Number(box.options[box.selectedIndex].value);

    var url = "/api/todos/" + toDoId + "/addref/" + targetId;
    console.log(url);

    console.log(targetId);

    $.ajax({
        type: 'post',
        url: url,
        contentType: 'text/html; charset=utf-8',
        data: targetId,
        dataType: 'json'}).done(function createToDoSuccess(data) {
        console.log("success");
        window.location.reload();
    }).fail(function createToDoFail() {
        console.log("fail");
        alert("참조를 걸 수 없습니다.");
    });
}

function completeToDo(e) {
    var toDoId = $(this).val();
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
        // $(e.target).closest(".complete").css('display', 'none');
        $(e.target).closest(".complete").text('completed');
        $(e.target).closest(".content-domain").css('background-color', 'black');
    }).fail(function createToDoFail() {
        console.log("fail");
        alert("선행 할일들을 먼저 마쳐주세요.");
        window.location.reload();
    });
}

function editTitle(e) {
    var newTitle = $(this).val();
    console.log(newTitle);
    var toDoId = $(this).closest(".content-domain").val();
    console.log(toDoId);
    var url = "/api/todos/" + toDoId;
    $.ajax({
        type: 'put',
        url: url,
        contentType: 'text/html; charset=utf-8',
        data: newTitle,
        dataType: 'json'}).done(function createToDoSuccess(data) {
        console.log("success");
        window.location.reload();
    }).fail(function createToDoFail() {
        console.log("fail");
        alert("이름 변경 실패. 빈칸은 실패합니다.");
        window.location.reload();
    });
}