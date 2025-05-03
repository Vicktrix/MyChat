//----------------------- INIT PAGE --------------------------------
document.addEventListener('DOMContentLoaded', e => {
    setTimeout(() => getListChats(), 500);
    setTimeout(() => getListUsers(), 600);
    setTimeout(() => listenChat(), 1000);
});
const getActualChatName= () => document.querySelector('#chat').value;
const getActualUserName= () => document.querySelector('#user').value;
const mypost = document.querySelector('#mypost');
const postBtn = document.querySelector('#send');
const posts = document.querySelector('#posts');
const selectUser = document.getElementById('user');
const selectChat = document.getElementById('chat');
const chatsArray = new Array("chat5");
const usersArray = new Array();
let eventSource;
function listenChat() {
    console.log('Inside eventSource');    
    if (eventSource !== undefined) { eventSource.close();    }
    eventSource = new EventSource(`http://localhost:8080/chat/init/${getActualUserName()}`);
    
    eventSource.onopen = (event) => {
        console.log("Server Sent Event init");
    };  
        console.log('before for in eventSource');
        for (let chat of chatsArray) {
            eventSource.addEventListener(chat, e => {
                console.log(e.data);
                console.log(e.lastEventId);
                addToChat(chat,e.data);
                showChat(chat);
            });          
        };
    eventSource.onerror = (error) => {
        console.error("Error happened, will be closed. Error :", error);
        eventSource.close(); 
    };
};
function getListUsers() {
    console.log('Inside getListUsers');
    let url = `http://localhost:8080/chat/users`;
    fetch(url)
        .then(checkErrors)
        .then(res => parseResponse(res, usersArray, addPresentUser))
        .catch(showError);
}
function getListChats() {
    console.log('Inside getListChats');
    let url = `http://localhost:8080/chat/chats/${getActualUserName()}`;
    fetch(url)
        .then(checkErrors)
        .then(res => parseResponse(res, chatsArray))
        .catch(showError);
}
//const parseResponse = (res, arr) => {
const parseResponse = (res, arr, and) => {
    console.log('inside parseResponse');
        res.split('\n')
            .filter(line => line.startsWith('data:'))
            .map(line => line.substring(5))
//            .forEach(s => arr.push(s));
            .forEach(s => {
                arr.push(s);
                if(and !== undefined) {and(s);}
            });
//        console.log(' Array : ');
//        arr.forEach(console.log);
};
const checkErrors = res => {
    if(!res.ok) { 
        return res.text().then(text => { 
            throw new Error(`Error from server : ${text}`);});
    }
    return res.text();
};
const showError = e => console.log('Error happened!!!', e);
postBtn.addEventListener('click', (e) => {
    writeToChat(mypost.value);
});
const writeToChat = message => {
    let url = `http://localhost:8080/chat/${getActualUserName()}/${getActualChatName()}`;
    fetch(url, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'}, 
        body: message})
    .then(checkErrors)
    .then(date => document.querySelector('#mypost').value="")
    .catch(showError);
};
const addToChat = (chat, message) => {
    const div = document.createElement("div");
    div.classList.add("card");
    div.classList.add(chat);
    div.innerText = message;
    const post = document.querySelector('#posts');
    post.append(div);
};
const addPresentUser = user => {
    const div = document.createElement("div");
    div.classList.add("card");
    div.innerText = user;
    const post = document.querySelector('#presents');
    post.append(div);
};
const showChat = chat => {
    const cards = document.querySelectorAll('#posts > .card');
        Array.from(cards).forEach(c => {
//            if(c.classList.contains(chat))  c.style.display = 'block';
            if(c.classList.contains(selectChat.value))  c.style.display = 'block';
            else c.style.display = 'none';
    });
};
selectChat.addEventListener('change',() => showChat(selectChat.value));
// -----------------------------------------------------------------------------
//selectUser.addEventListener('change',() => listenChat());
selectUser.addEventListener('change',listenChat);