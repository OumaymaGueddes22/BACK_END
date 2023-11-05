'use strict';


var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];


const loginForm = document.querySelector("form");
const errorMessage = document.getElementById("error-message");

function connect(event) {
    var nameInput = document.querySelector('#name');
    if (nameInput) {
        username = nameInput.value.trim();

        if (username) {
            usernamePage.classList.add('hidden');

            chatPage.classList.remove('hidden');

            var socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);

            stompClient.connect({}, onConnected, onError);
        }
    }
    event.preventDefault();
}



function onConnected() {
    var authResponse = JSON.parse(localStorage.getItem("authResponse"));
    var userId = authResponse.firstname;
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/mesg.sendMessage",
        {},
        JSON.stringify({ sender: userId, type: 'JOIN' }) // Use sender's first name
    );

    connectingElement.classList.add('hidden');

    // Make the chat page visible
    chatPage.classList.remove('hidden');
}



function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const phoneNumber = event.target.querySelector("input[name=phoneNumber]").value;
    const password = event.target.querySelector("input[name=password]").value;

    const requestData = {
        phoneNumber: phoneNumber,
        password: password
    };

    const response = await fetch("/api/v1/auth/authenticate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData),
    });

    if (response.ok) {
        const authResponse = await response.json();
        localStorage.setItem("authResponse", JSON.stringify(authResponse));
        localStorage.setItem("senderFirstName", authResponse.firstName); // Store the sender's first name
        // Establish a WebSocket connection here
        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onConnected, onError);
    } else {
        errorMessage.textContent = "Authentication failed. Please check your phone number and password.";
        errorMessage.style.display = "block";
        event.target.reset();
    }
});



function send(event) {
    var authResponse = JSON.parse(localStorage.getItem("authResponse"));
    var firstname = authResponse.firstname;
    var userId = authResponse.id;


    console.log("azerty",userId)
    var txtContent = messageInput.value.trim();

    if (txtContent && stompClient) {
        var chatMessage = {
            txt: txtContent,
            sender: firstname,
            type: "CHAT"
        };

        // Mettez l'ID utilisateur dans le chemin de l'URL de destination
        var destination = "/app/chat.send/" + userId;

        stompClient.send(destination, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var txt = JSON.parse(payload.body); // Change variable name to txt

    var messageElement = document.createElement('li');

    if (txt.type === 'JOIN') {
        messageElement.classList.add('event-message');
        txt.txt = txt.sender + ' joined!'; // Change attribute name to txt
    } else if (txt.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        txt.txt = txt.sender + ' left!'; // Change attribute name to txt
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(txt.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(txt.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(txt.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var textElement = document.createElement('p');
    var messageText = document.createTextNode(txt.txt);  // Change attribute name to txt
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}

function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', send, true);
