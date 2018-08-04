var kafka = require('kafka-node');
var KeyedMessage = kafka.KeyedMessage;
var Producer = kafka.Producer;
var client = new kafka.Client('127.0.0.1:2181');
var producer = new Producer(client);
console.log('Producer 连接kafka中');

class kafkaProducer {

    static produce(key, message, cb) {
    
        let payloads = [
            { topic: 'micromsg', messages: new KeyedMessage(key, message) }
        ];

        producer.on('ready', function () {
            console.log("ready")
        });

        producer.send(payloads, function (err, data) {
            if (!!err){
                console.log(err)
            }
            cb(data);
        });
    }
}

module.exports = kafkaProducer;