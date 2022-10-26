<template>
    <section v-if="inLogin" class="mt-5">
        <div class="d-flex justify-content-center">
            <div class="w-50">
                <div class="card rounded-5">
                    <div class="card-body">
                        <label class="form-label" for="usernameField">Username:</label>
                        <input class="form-control rounded-4" id="usernameField" type="text"
                               v-model.trim="username" placeholder="Enter your username">
                        <button class="btn btn-dark mt-2 rounded-5 w-100" type="button"
                        @click="connect">Connect</button>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section v-else class="mt-5">
        <div class="d-flex justify-content-center">
            <div class="w-75">
                <div class="card rounded-5">
                    <div class="card-body">
                        <h6>Online now:</h6>
                        <ul class="border border-1 rounded-3">
                            <template v-for="(u, i) in users" :key="i">
                                <li class="text-success"  v-if="username !== u.username">
                                    {{ u.username }}
                                </li>
                            </template>
                        </ul>
                        <h6>CHAT:</h6>
                        <ul class="border border-1 rounded-3">
                            <li class="nav-item mb-3" v-for="(m,i) in messages" :key="i">
                                <template v-if="m.chatType === 'JOIN' || m.chatType === 'LEAVE'">
                                    <span class="text-primary">{{ m.sender + ' ' +m.chatType }} </span>
                                </template>
                                <template v-else>
                                    <b class="text-danger">{{ m.sender }}:</b>
                                    <span class="p-1">
                                    {{ m.message }}
                                </span>
                                </template>
                            </li>
                        </ul>
                    </div>
                    <div class="card-body">
                        <input class="form-control rounded-4" id="messageField" type="text"
                               v-model.trim="myMessage" placeholder="Message">
                        <button class="btn btn-dark mt-2 rounded-5 w-100" type="button"
                                @click="send">Send</button>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script setup>

import {ref} from "vue";
import SockJS from "sockjs-client/dist/sockjs.min";
import {Stomp} from "@stomp/stompjs";

const username = ref()
const stomp = ref()
const myMessage = ref('')
const inLogin = ref(true)
const messages = ref([])
const currentMessage = ref({sender: '', chatType: '', message:''})
const users = ref([])


const URL = "http://localhost:8080/connect"
const URL_ACTIVE = "http://localhost:8080/active"

const connect = () => {
    if (username.value){
        console.log(username.value)
        let socket = new SockJS(URL)
        stomp.value = Stomp.over(socket)
        stomp.value.connect({}, connected)
    }
}

const connected = () => {
    stomp.value.subscribe("/topic/all", sendMessage)
    stomp.value.send("/app/chat.logIn",{},
        JSON.stringify({sender: username.value, chatType: 'JOIN'})
    )
    inLogin.value = false

}

const sendMessage = (payload) => {
    const message = JSON.parse(payload.body);
    messages.value.push(message)

    currentMessage.value = message
    console.log(messages.value)
    if(message.chatType === 'JOIN'){
        console.log("JOIN")
        listActiveUsers()
    } else if (message.chatType === 'LEAVE'){
        console.log('LEAVE')
        listActiveUsers()
    } else {
        console.log('SENDER: '+message.sender)
        console.log('MESSAGE: '+message.message)
    }

}

function send(){
    if(myMessage.value && stomp.value){
        const userMessage = {
            message: myMessage.value,
            chatType: 'CHAT',
            sender: username.value
        };
        stomp.value.send("/app/chat.send",{},JSON.stringify(userMessage))
        myMessage.value = ''
    }
}

function listActiveUsers(){
    var xhr = new XMLHttpRequest();
    xhr.open("GET", URL_ACTIVE);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log('tttttttttttt')
            users.value = JSON.parse(xhr.responseText);
        }
    };
    xhr.send();
}
// function showActiveUser(users){
//     document.getElementById('test').remove();
//     var mainDiv = document.createElement('div');
//     mainDiv.classList.add('abso');
//     mainDiv.id = 'test';
//     for (let z=0;z<users.length;z++){
//         var div = document.createElement('div');
//         var span1 = document.createElement('span');
//         span1.classList.add('name-us');
//         var userName = document.createTextNode(users[z].username);
//         span1.appendChild(userName);
//         var span2 = document.createElement('span');
//         var i = document.createElement('i');
//         i.classList.add("fas");
//         i.classList.add("fa-circle");
//         span2.appendChild(i);
//         div.appendChild(span1);
//         div.appendChild(span2);
//         mainDiv.appendChild(div);
//     }
//     main.appendChild(mainDiv)
//
// }
</script>

<style scoped>

</style>