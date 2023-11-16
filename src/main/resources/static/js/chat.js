'use strict';


var usersArea = document.querySelector('.chat-list');

function loadUser() {
    fetch("/api/v1/users/allUsers")
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(users => {
            displayUsers(users);
        })
        .catch(error => {
            console.error('Error fetching messages:', error);
        });
}

function displayUsers(users){
    users.forEach(user => {
        let userHtml =
            "<img src=\"/api/v1/auth/users/files/"+user.image+"\" class =\"user-img\" alt=\"avatar\">\n" +
            "<div class=\"about\">\n" +
            "    <div class=\"name\">"+user.fullName+" </div>\n" +
            "    <div class=\"status\"> <i class=\"fa fa-circle online\"></i> online </div>\n" +
            "</div>\n" ;
        let userDom = document.createElement("li")
        userDom.classList.add("clearfix")
        userDom.innerHTML = userHtml;
        usersArea.appendChild(userDom);

    });
}
