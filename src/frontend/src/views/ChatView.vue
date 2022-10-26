<template>
    <section class="mt-5">
        <div class="d-flex justify-content-center">
            <div class="w-75">
                <div class="card rounded-5">
                    <div class="card-body">
                        <ul>
                            <li class="nav-item mb-3">
                                <b class="text-danger">OTHMANE:</b>
                                <span class="p-1">
                                    sdqhf qjksd hfqlskjdfh lqsdjh flkqdsjhqklsmdjf qmsdlkjf qsdmlkjf lqsdkjf mlqsdkjf lmdsqkjf lmkdsqjf lmkqdsjf lkqdsjf lmkqdsjflkdqsjf lqdskj ff lkqsdj fhlqskdj fh
                                </span>
                            </li>
                            <li class="nav-item">
                                <b class="text-danger">MUHAMMED:</b>
                                <span class="p-1">
                                    sdqhf qjksd hfqlskjdfh lqsdjh flkqdsjhf lkqsdj fhlqskdj fh
                                </span>
                            </li>
                        </ul>
                    </div>
                    <div class="card-body">
                        <input class="form-control rounded-4" id="usernameField" type="text"
                               v-model.trim="username" placeholder="Message">
                        <button class="btn btn-dark mt-2 rounded-5 w-100" type="button"
                                @click="connect">Send</button>
                    </div>
                </div>
            </div>
        </div>
    </section>
</template>

<script setup>

const props = defineProps({sender: String})

function sendMessage(payload){
    var message = JSON.parse(payload.body)
    if(message.chatType == 'JOIN'){
        joinUser(message,"join")
        listActiveUsers()
    } else if (message.chatType == 'LEAVE'){
        joinUser(message,"leave")
        listActiveUsers()
    } else {
        var li = document.createElement('li');
        li.classList.add('sms');
        var image = document.createElement('img');
        image.src = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png";
        var span1 = document.createElement('span');
        span1.classList.add('my-message');
        var span2 = document.createElement('span');
        span2.classList.add('user');
        var span2User = document.createTextNode(message.sender);
        span2.appendChild(span2User)
        var span3 = document.createElement('span');
        span3.classList.add('mes');
        var span3Message = document.createTextNode(message.message);
        span3.appendChild(span3Message)
        span1.appendChild(span2);
        span1.appendChild(span3);
        li.appendChild(image);
        li.appendChild(span1);
        mainChat.appendChild(li);
    }
}
function joinUser(message,state){
    var li1 = document.createElement('li');
    var li2 = document.createElement('li');
    var hr1 = document.createElement('hr');
    var hr2 = document.createElement('hr');
    var messageJoin = document.createTextNode(message.sender + " " + state)
    li1.classList.add('status');
    li1.appendChild(messageJoin)
    li2.appendChild(hr1)
    li2.appendChild(li1)
    li2.appendChild(hr2)
    mainChat.appendChild(li2)
}
function send(){
    var messageUser = document.querySelector('#sms').value.trim()     //
    if(messageUser && stomp){
        var userMessage ={
            message: messageUser,
            chatType:'CHAT',
            sender: userName
        }
        stomp.send("/app/chat.send",{},JSON.stringify(userMessage))
        document.querySelector('#sms').value = '';
    }
}

</script>

<style scoped>

</style>