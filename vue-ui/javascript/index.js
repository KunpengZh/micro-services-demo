Vue.component('springboot202', {
    data: function () {
        return {
            springmessage: {
                issue: "",
                name: "",
                email: ""
            },
            message: ""
        }
    },
    methods: {
        refreshMessage(resource) {
            this.$http.get('/springboot202/user/getUserProfile').then((response) => {
                console.log(response)
                res = response.data;
                this.springmessage.issue = res.iss;
                this.springmessage.name = res.name;
                this.springmessage.email = res.sub;
            });
        },
        sendMessage() {
            this.$http.post('/springboot202/kafka/send', this.message).then((res) => {
                console.log(res);
            })
        }
    },
    template: `
    <div class="springboot202">
    <h1>Springboot 202 services</h1>
    <h1>==========================================================</h1>
    <h3>Get User Profile from Springboot202</h3>
    <button v-on:click="refreshMessage">Get from Spring</button>
    <div id="userProfileDisplay">
        <p><strong>issue:</strong>{{springmessage.issue}}</p>
        <p><strong>name:</strong>{{springmessage.name}}</p>
        <p><strong>email:</strong>{{springmessage.email}}</p>
    </div>
    <h3>Sent a message to Kafaka message borker by Springboot</h3>
    <input id="msgInput" v-model="message"></input>
    <button v-on:click="sendMessage">Sent Message</button>
    </div>
    `
});
Vue.component('nodejs', {
    data: function () {
        return {
            nodejsmessage: {
                issue: "",
                name: "",
                email: ""
            },
            message: ""
        }
    },
    methods: {
        refreshMessage(resource) {
            this.$http.get('/nodejs/user/getUserProfile').then((response) => {
                console.log(response)
                res = response.data;
                this.nodejsmessage.issue = res.iss;
                this.nodejsmessage.name = res.name;
                this.nodejsmessage.email = res.sub;
            });
        },
        sendMessage() {
            this.$http.post('/nodejs/user/sendKafkaMsg', { message: this.message }).then((res) => {
                console.log(res);
            })
        }
    },
    template: `
    <div class="nodejs">
        <h1>NodeJs services</h1>
        <h1>==========================================================</h1>
        <h3>Get a message from Nodejs services</h3>
        <button v-on:click="refreshMessage">Get from Nodesjs</button>
        <div id="nodejsmsg">
        <p><strong>issue:</strong>{{nodejsmessage.issue}}</p>
        <p><strong>name:</strong>{{nodejsmessage.name}}</p>
        <p><strong>email:</strong>{{nodejsmessage.email}}</p>
        </div>
        <h3>Sent a message to Kafaka message borker by Node</h3>
        <input id="msgInput" v-model="message"></input>
        <button v-on:click="sendMessage">Sent Message</button>
    </div>
    `
});

var app = new Vue({
    el: '#app',
    data: {

    }
})
