package org.example.Consumer;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerCooperativeBalance {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerCooperativeBalance.class);

    public static void main(String[] args) {
        LOGGER.info(" Kafka Consume");

        String bootstrapServers = "127.0.0.1:9092";
        String groupId = "my-first-group";
        String topic = "demo-java";

        // set the Consumer Properties

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, CooperativeStickyAssignor.class.getName());

        // Create the Consumer
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(properties);

        // get a reference to the current thread

        final Thread mainThread = Thread.currentThread();
        // adding the shutdown hook

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOGGER.info("Detected a Shutdown,lets exits by calling consumer.wakeup()......");
                kafkaConsumer.wakeup();

                // join the main thread to allow the execution of the code in the main thread
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        //Consume the message --subscribe
        kafkaConsumer.subscribe(Arrays.asList(topic));

        try {
            // poll for new data
            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> consumerRecord : records) {
                    LOGGER.info("Key: " + consumerRecord.key() + " Value: " + consumerRecord.value());
                    LOGGER.info("Partition: " + consumerRecord.partition() + " Offset: " + consumerRecord.offset());
                }
            }

        } catch (WakeupException e)
        {
            LOGGER.info("Wake Up Exception");
        }
        catch (Exception e)
        {
            LOGGER.error("Unexpected exception");
        }
        finally {
            kafkaConsumer.close();
            LOGGER.info("the Consumer is now Gracefully closed ");
        }


    }
}
