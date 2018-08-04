package com.ibm.cio.sets.procurement.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class KafkaSender {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Gson gson = new GsonBuilder().create();

    public void send(String msg) {
       KafkaMessage kafkaMessage=new KafkaMessage();
        kafkaMessage.setId(UUID.randomUUID().toString());
        kafkaMessage.setMsg(msg);
        kafkaMessage.setSendTime(new Date());
        System.out.println("+++++++++++++++++++++  message:"+ gson.toJson(kafkaMessage).toString());
        kafkaTemplate.send("micromsg", "thetopickey",gson.toJson(kafkaMessage));
    }
}
