//----------------------- INIT PAGE --------------------------------
document.addEventListener('DOMContentLoaded', e => {
//    setTimeout(myFunction, 1000);
//    function myFunction() {  
//        let actualChatName = document.querySelector('#chat').value;
//        getChat(actualChatName);
//    };
    setTimeout(() => getChat(getActualChatName()), 1000);
});
const getActualChatName= () => document.querySelector('#chat').value;
//---------------------- MY EVENTS ---------------------------------
function getChat(chatName) {
    const eventSource = new EventSource(`http://localhost:8080/chat/${chatName}`);
    eventSource.onopen = (event) => {
        console.log("Server Sent Event init");
    };
    eventSource.addEventListener(chatName, e => {
        console.log(e.data);
        console.log(e.lastEventId);
        addMessage(e.data);
    });
    eventSource.onerror = (error) => {
        console.error("Error happened, will be closed. Error :", error);
        eventSource.close(); 
    };
};
//----------------------------------------------------
const addMessage = message => {
    const div = document.createElement("div");
    div.classList.add("card");
    div.innerText = message;
    const post = document.querySelector('#posts');
    post.append(div);
};
const postBtn = document.querySelector('#send');

//postBtn.addEventListener('click', (e) => {
//    const mypost = document.querySelector('#mypost');
//    const div = document.createElement("div");
//    div.classList.add("card");
//    div.innerText = mypost.value;
//    const post = document.querySelector('#posts');
//    post.append(div);
//});
postBtn.addEventListener('click', (e) => {
    const mypost = document.querySelector('#mypost');
    const div = document.createElement("div");
    div.classList.add("card");
    postMessage(mypost.value)
//    div.innerText = mypost.value;
//    const post = document.querySelector('#posts');
//    post.append(div);
});
const postMessage = message => {
    let url = "http://localhost:8080/chat/post/"+getActualChatName();
    fetch(url, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'}, 
        body: message})
    .then(res => {
        if(!res.ok) { 
            return res.text().then(text => { 
                throw new Error(`Error from server : ${text}`);});
        }
            console.log('Inside get gson');
        return res.text();
    })
    .then(addMessage)
    .catch(e => console.log('Error happened!!!'));
};