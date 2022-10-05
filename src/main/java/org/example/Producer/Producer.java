package org.example.Producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Producer {


    private static final Logger  LOGGER = LoggerFactory.getLogger(Producer.class.getName());


    public static void main(String[] args) {


        LOGGER.info("Kafka Producer");

        String bootstrapServers="127.0.0.1:9092";

        // create Produce Properties

        Properties properties =new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        // create the Producer
        KafkaProducer<String,String> kafkaProducer = new KafkaProducer<>(properties);

        // create Producer Record
        ProducerRecord<String,String> producerRecord = new ProducerRecord<>("demo-java","hello-world");
        // send the data- asynchronous process
        kafkaProducer.send(producerRecord);

        // flush and close the Producer
        kafkaProducer.flush();
        kafkaProducer.close();
    }
}
