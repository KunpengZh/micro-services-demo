'use strict';
var kafkaConsumer = function () {
    var kafka = require('kafka-node');
    var Consumer = kafka.Consumer;
    //var client = new kafka.Client('10.251.5.166:12181,10.171.27.37:12181,10.170.177.146:12181');
    var client = new kafka.Client('127.0.0.1:2181'); //测试
    var Offset = kafka.Offset;
    var offset = new Offset(client);
    console.log('Consumer 连接kafka中');
    var topics = [{
        topic: 'micromsg',offset: 0
    }];
    var options = {
// Auto commit config
        autoCommit: true,
        autoCommitMsgCount: 100,
        autoCommitIntervalMs: 1000,
// Fetch message config
        fetchMaxWaitMs: 100,
        fetchMinBytes: 1,
        fetchMaxBytes: 1024 * 10,
        fromOffset: true,
        fromBeginning: false
    };
    var consumer = new Consumer(
        client,
        topics,
        options
    );
    consumer.on('message', function (message) {
        
        try {
            var msg = JSON.parse(message.value);
        } catch (e) {
            msg=message.value;
        }
        console.log(msg);
    });

    consumer.on('error', function (message) {
        console.log(message);
        console.log('kafka错误');
    });
};
module.exports = kafkaConsumer;