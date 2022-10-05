package org.example.Consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

public class Consumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) {
        LOGGER.info(" Kafka Consume");

        String bootstrapServers="127.0.0.1:9092";
        String groupId = "my-first-group";
        String topic = "demo-java";

        // set the Consumer Properties

        Properties properties =new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");

        // Create the Consumer
        KafkaConsumer<String,String> kafkaConsumer =new KafkaConsumer(properties);

        //Consume the message --subscribe
        kafkaConsumer.subscribe(Arrays.asList(topic));

        // poll for new data
        while (true)
        {
            LOGGER.info("Polling....");
            ConsumerRecords<String,String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String,String> consumerRecord :records)
            {
                LOGGER.info("Key: "+ consumerRecord.key() + " Value: " + consumerRecord.value());
                LOGGER.info("Partition: "+ consumerRecord.partition() + " Offset: " + consumerRecord.offset());
            }
        }



    }
}
