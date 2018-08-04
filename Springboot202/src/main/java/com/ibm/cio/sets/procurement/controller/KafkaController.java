package com.ibm.cio.sets.procurement.controller;

import com.ibm.cio.sets.procurement.kafka.KafkaSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("kafka")
public class KafkaController {

    @Autowired
    KafkaSender kafkaSender;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public String sendkafka(@RequestBody String postedMsg) throws Exception {

        System.out.println(postedMsg);
        kafkaSender.send(postedMsg);

        return "successed";
    }

    @RequestMapping(value = "/recmsg")
    public String recmsg(){
        return "hello";
    }
}
