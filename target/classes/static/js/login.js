'use strict';

loadUser();

var usersArea = document.querySelector('.chat-list');
var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var container = document.querySelector('.container');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];


const loginForm = document.querySelector("form");
const errorMessage = document.getElementById("error-message");

checkToken();
function connect(event) {
    var nameInput = document.querySelector('#name');
    if (nameInput) {
        username = nameInput.value.trim();

        if (username) {

            usernamePage.classList.add('hidden');
            chatPage.classList.remove('hidden');
            usersArea.classList.remove('hidden');
            container.classList.remove('hidden');
            var socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);

            stompClient.connect({}, onConnected, onError);
        }
    }
    event.preventDefault();
}

var pageSize = 10;
var currentPage = 1;

function loadMoreMessages() {
    getMessagesUser();
}

function getMessagesUser() {
    var authResponse = JSON.parse(localStorage.getItem("authResponse"));
    var userId = authResponse.id;
    var destination = "public";

    if (currentPage < 1) {
        currentPage = 1;
    }

    if (pageSize < 1) {
        pageSize = 10;
    }

    fetch("/getMessagesUser/" + userId + "/"+ destination +"?page=" + currentPage + "&pageSize=" + pageSize)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(messages => {
            displayMessages(messages);
        })
        .catch(error => {
            console.error('Error fetching messages:', error);
        });
}

function displayMessages(messages) {
    if (!messages || !Array.isArray(messages)) {
        console.error('Invalid messages data:', messages);
        return;
    }
    let scrollHeight = messageArea.scrollHeight
    messages.forEach(message => {
        var messageElement = document.createElement('li');

        if (!message || typeof message !== 'object') {
            console.error('Invalid message:', message);
            return;
        }

        if (message.type === 'JOIN' || message.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            messageElement.textContent = message.sender + (message.type === 'JOIN' ? ' joined!' : ' left!');
        } else {
            messageElement.classList.add('chat-message');

            var avatarElement = document.createElement('i');
            var avatarText = document.createTextNode(message.sender ? message.sender[0] : '');
            avatarElement.appendChild(avatarText);

            var avatarColor = getAvatarColor(message.sender);
            avatarElement.style['background-color'] = avatarColor || 'defaultColor';

            messageElement.appendChild(avatarElement);

            var usernameElement = document.createElement('span');
            var usernameText = document.createTextNode(message.sender || '');
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);

            var textElement = document.createElement('p');

            if (message.txt !== null && message.txt !== undefined) {
                var messageText = document.createTextNode(message.txt);
                textElement.appendChild(messageText);
            }

            if (message.pdfContent) {
                var pdfElement = document.createElement('embed');
                pdfElement.src = "data:application/pdf;base64," + message.pdfContent;
                pdfElement.type = 'application/pdf';
                pdfElement.width = 320;
                pdfElement.height = 240;
                textElement.appendChild(pdfElement);

                //bech pdf yhbet
                var downloadLink = document.createElement('a');
                downloadLink.href = "data:application/pdf;base64," + message.pdfContent;
                downloadLink.download = "document.pdf";
                downloadLink.textContent = "Download PDF";
                textElement.appendChild(downloadLink);
            }


            if (message.imageContent) {
                var imageElement = document.createElement('img');
                imageElement.src = "data:image/jpeg;base64," + message.imageContent;
                imageElement.width = 320;
                imageElement.height = 240;
                textElement.appendChild(imageElement);
            }

            if (message.videoContent) {
                var videoElement = document.createElement('video');
                videoElement.controls = true;
                videoElement.width = 320;
                videoElement.height = 240;
                var sourceElement = document.createElement('source');
                sourceElement.src = "data:video/mp4;base64," + message.videoContent;
                sourceElement.type = 'video/mp4';
                videoElement.appendChild(sourceElement);
                textElement.appendChild(videoElement);
            }

            if (message.audioContent) {
                var audioElement = document.createElement('audio');
                audioElement.controls = true;
                var sourceElement = document.createElement('source');
                sourceElement.src = "data:audio/mp3;base64," + message.audioContent;
                sourceElement.type = 'audio/mp3';
                audioElement.appendChild(sourceElement);
                textElement.appendChild(audioElement);
            }
            /* var time = document.createTextNode(message.time);
             textElement.appendChild(time);*/

            messageElement.appendChild(textElement);
        }
        messageArea.insertBefore(messageElement, messageArea.firstChild);

    });
    if(currentPage == 1){
        messageArea.scrollTop = scrollHeight;
    }else{
        messageArea.scrollTop = -scrollHeight;
    }

    if (messages.length < pageSize) {
        var loadMoreButton = document.getElementById('loadMoreButton');
        if (loadMoreButton) {
            loadMoreButton.style.display = 'none';
        }
    } else {
        var loadMoreButton = document.getElementById('loadMoreButton');
        if (loadMoreButton) {
            loadMoreButton.style.display = 'block';
        }
    }

    currentPage += 1;
}

function onConnected() {
    var authResponse = JSON.parse(localStorage.getItem("authResponse"));
    var userId = authResponse.id;
    stompClient.subscribe('/topic/public', onMessageReceived);

    connectingElement.classList.add('hidden');
    getMessagesUser();
    stompClient.subscribe('/topic/private/' + userId, function (payload) {

    });

    chatPage.classList.remove('hidden');
    usersArea.classList.remove('hidden');
    container.classList.remove('hidden');

}




function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}



loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const phoneNumber = event.target.querySelector("input[name=phoneNumber]").value;
    const password = event.target.querySelector("input[name=password]").value;

    const response = await fetch("/api/v1/auth/authenticate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            phoneNumber,
            password,
        }),
    });

    if (response.ok) {
        const authResponse = await response.json();
        localStorage.setItem("authResponse", JSON.stringify(authResponse));
        connectToWS();

    } else {

        errorMessage.textContent = "Authentification échouée. Veuillez vérifier votre phoneNumber et votre mot de passe.";
        errorMessage.style.display = "block";

        // Clear the form fields
        event.target.reset();
    }
});

function connectToWS(){
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

async function checkToken() {
    let authResponse = localStorage.getItem("authResponse");
    if (authResponse) {
        let token = JSON.parse(authResponse)['access_token'];
        const requestData = {token: token};
        const response = await fetch("/api/v1/auth/check-token", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                'Authorization': token
            },
            body: JSON.stringify(requestData),
        });
        if (response.ok) {
            const data = await response.json();
            let isValid =  data.valid;
            if(isValid){
                connectToWS();
            }
        }else {

            chatPage.classList.add('hidden');
            usersArea.classList.add('hidden');
            container.classList.add('hidden');
            usernamePage.classList.remove('hidden');
        }
    }else{
        chatPage.classList.add('hidden');
        usersArea.classList.add('hidden');
        container.classList.add('hidden');
        usernamePage.classList.remove('hidden');
    }
}


function send(event) {
    var authResponse = JSON.parse(localStorage.getItem("authResponse"));
    var firstname = authResponse.firstname;
    var userId = authResponse.id;

    var txtContent = messageInput.value.trim();
    var imageFileInput = document.getElementById('imageInput');
    var videoFileInput = document.getElementById('videoInput');
    var pdfFileInput = document.getElementById('pdfInput'); // Add input for PDF files

    if ((txtContent || imageFileInput.files.length > 0 || videoFileInput.files.length > 0 || pdfFileInput.files.length > 0) && stompClient) {
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
                imageFileInput.value = '';
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
        } else if (pdfFileInput.files.length > 0) {
            var reader = new FileReader();
            reader.onload = function (e) {
                chatMessage.pdfFileName = pdfFileInput.files[0].name;
                chatMessage.pdfContent = e.target.result.split(",")[1];
                stompClient.send(destination, {}, JSON.stringify(chatMessage));
                messageInput.value = '';
                pdfFileInput.value = '';
            };
            reader.readAsDataURL(pdfFileInput.files[0]);
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
        messageElement.classList.add('event-message');
        message.txt = message.sender + ' joined!';
    } else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.txt = message.sender + ' left!';
    } else {
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);

        var contentElement = document.createElement('div');

        if (message.videoContent) {
            var videoElement = document.createElement('video');
            videoElement.controls = true;
            videoElement.width = 320;
            videoElement.height = 240;
            var sourceElement = document.createElement('source');
            sourceElement.src = "data:video/mp4;base64," + message.videoContent;
            sourceElement.type = 'video/mp4';
            videoElement.appendChild(sourceElement);
            contentElement.appendChild(videoElement);
        } else if (message.imageContent) {
            var imageElement = document.createElement('img');
            imageElement.src = "data:image/jpeg;base64," + message.imageContent;
            imageElement.width = 320;
            imageElement.height = 240;
            contentElement.appendChild(imageElement);
        } else if (message.pdfContent) {
            var pdfElement = document.createElement('embed');
            pdfElement.src = "data:application/pdf;base64," + message.pdfContent;
            pdfElement.type = 'application/pdf';
            pdfElement.width = 320;
            pdfElement.height = 240;
            contentElement.appendChild(pdfElement);
        }
        else if (message.audioContent) {
            var audioElement = document.createElement('audio');
            audioElement.controls = true;
            var sourceElement = document.createElement('source');
            sourceElement.src = "data:audio/mp3;base64," + message.audioContent;
            sourceElement.type = 'audio/mp3';
            audioElement.appendChild(sourceElement);
            contentElement.appendChild(audioElement);
        }
        else {
            var messageText = document.createTextNode(message.txt);
            contentElement.appendChild(messageText);
        }

        messageElement.appendChild(contentElement);
    }

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}



function getAvatarColor(messageSender) {
    if (!messageSender) {
        console.error('Invalid message sender:', messageSender);
        return 'defaultColor';
    }

    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', send, true);

document.getElementById("startRecording").addEventListener("click", initFunction);
let isRecording = document.getElementById("isRecording");

function initFunction() {
    // Function to handle user media
    async function getUserMedia(constraints) {
        if (window.navigator.mediaDevices) {
            return window.navigator.mediaDevices.getUserMedia(constraints);
        }
        let legacyApi =
            navigator.getUserMedia ||
            navigator.webkitGetUserMedia ||
            navigator.mozGetUserMedia ||
            navigator.msGetUserMedia;
        if (legacyApi) {
            return new Promise(function (resolve, reject) {
                legacyApi.bind(window.navigator)(constraints, resolve, reject);
            });
        } else {
            alert("user api not supported");
        }
    }

    isRecording.textContent = "Recording...";

    let audioChunks = [];
    let rec;

    function handlerFunction(stream) {
        rec = new MediaRecorder(stream);
        rec.start();

        rec.ondataavailable = (e) => {
            audioChunks.push(e.data);

            if (rec.state == "inactive") {
                let blob = new Blob(audioChunks, { type: "audio/mp3" });
                console.log(blob);

                // Convert audio blob to base64
                const reader = new FileReader();
                reader.onloadend = function () {
                    const base64data = reader.result.split(',')[1];

                    sendAudioDataToServer(base64data);
                };
                reader.readAsDataURL(blob);
            }
        };
    }

    function startUsingBrowserMicrophone(boolean) {
        getUserMedia({ audio: boolean }).then((stream) => {
            handlerFunction(stream);
        });
    }

    startUsingBrowserMicrophone(true);

    function sendAudioDataToServer(audioData) {
        var authResponse = JSON.parse(localStorage.getItem("authResponse"));
        var senderId = authResponse.id;

        var firstname = authResponse.firstname;

        console.log("senderId:", senderId);

        var chatMessage = {
            sender: firstname,
            type: "CHAT"
        };

        var destination = "/app/chat.send/" + senderId;


        if (stompClient) {

            chatMessage.audioContent = audioData;


            stompClient.send(destination, {}, JSON.stringify(chatMessage));

            isRecording.textContent = "Click play button to start listening";
        }
    }

    // Stop recording handler
    document.getElementById("stopRecording").addEventListener("click", (e) => {
        rec.stop();
    });
}
