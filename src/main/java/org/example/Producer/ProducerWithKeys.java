package org.example.Producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


/*

 Send non-null keys to the kafka topics
 Same Key=same partition
 */


public class ProducerWithKeys {


    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerWithKeys.class.getName());


    public static void main(String[] args) {


        LOGGER.info("Kafka Producer with Callbacks");

        String bootstrapServers = "127.0.0.1:9092";

        // create Produce Properties

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // create the Producer
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        // create Producer Record
        // send the data- asynchronous process
        // send 10 records
        for (int i = 0; i < 10; i++) {

            String topic = "demo-java";
            String value = "hello World: " + i;
            String key = "_id: " + i;

            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, value);

            kafkaProducer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    // executes every time a record is successfully send or an exception is thrown
                    if (e == null) {
                        LOGGER.info("Received new Metadata"
                                + "\n" + "Topic: " + recordMetadata.topic()
                                + "\n" + "Key: " + producerRecord.key()
                                + "\n" + "Partition: " + recordMetadata.partition()
                                + "\n" + "Offset: " + recordMetadata.offset()
                                + "\n" + "TimeStamp: " + recordMetadata.timestamp()

                        );
                    } else {
                        LOGGER.error("Error While Producing", e);
                    }
                }
            });

          /*  // It goes to different Partition
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        }


        // flush and close the Producer
        kafkaProducer.flush();
        kafkaProducer.close();
    }
}
