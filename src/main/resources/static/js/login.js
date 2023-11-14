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

    stompClient.send("/app/mesg.sendMessage",
        {},
        JSON.stringify({ sender: userId, type: 'JOIN' }) // Use sender's first name
    );

    connectingElement.classList.add('hidden');

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

    var txtContent = messageInput.value.trim();
    var imageFileInput = document.getElementById('imageInput');
    var videoFileInput = document.getElementById('videoInput');

    if ((txtContent || imageFileInput.files.length > 0 || videoFileInput.files.length > 0) && stompClient) {
        var chatMessage = {
            txt: txtContent,
            sender: firstname,
            type: "CHAT"
        };

        var destination = "/app/chat.send/" + userId;

        if (imageFileInput.files.length > 0) {
            var reader = new FileReader();
            reader.onload = function (e) {
                chatMessage.imageFileName = imageFileInput.files[0].name;
                chatMessage.imageContent = e.target.result.split(",")[1];
                stompClient.send(destination, {}, JSON.stringify(chatMessage));
                messageInput.value = '';
                imageFileInput.value = ''; // Clear the file input
            };
            reader.readAsDataURL(imageFileInput.files[0]);
        } else if (videoFileInput.files.length > 0) {
            var reader = new FileReader();
            reader.onload = function (e) {
                chatMessage.videoFileName = videoFileInput.files[0].name;
                chatMessage.videoContent = e.target.result.split(",")[1];
                stompClient.send(destination, {}, JSON.stringify(chatMessage));
                messageInput.value = '';
                videoFileInput.value = ''; // Clear the file input
            };
            reader.readAsDataURL(videoFileInput.files[0]);
        } else {
            stompClient.send(destination, {}, JSON.stringify(chatMessage));
            messageInput.value = '';
        }
    }
    event.preventDefault();
}



function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

    if (message.type === 'JOIN') {
        // ... (existing code for JOIN messages)
    } else if (message.type === 'LEAVE') {
        // ... (existing code for LEAVE messages)
    }else if (message.videoContent) {
        // Handle video messages
        messageElement.classList.add('chat-message');

        var videoElement = document.createElement('video');
        videoElement.src = 'data:video/*;base64,' + message.videoContent;
        videoElement.controls = true;

        messageElement.appendChild(videoElement);

        var senderElement = document.createElement('span');
        senderElement.innerText = message.sender;
        messageElement.appendChild(senderElement);

        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    } else {
        // Handle text messages
        messageElement.classList.add('chat-message');

        var senderElement = document.createElement('span');
        senderElement.innerText = message.sender;
        messageElement.appendChild(senderElement);

        var textElement = document.createElement('p');
        textElement.innerText = message.txt;
        messageElement.appendChild(textElement);

        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    }
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

