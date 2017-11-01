package by.poptsov.testcontainers.services.persist.impl;

import by.poptsov.testcontainers.services.persist.PersistService;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testcontainers.shaded.com.google.common.collect.Lists;

import java.util.Properties;

/**
 * TODO: description
 *
 * @author Alexey Poptsov
 */
//@Service
public class KafkaPersistService implements PersistService {

    private static final String TOPIC = "STORAGE";

    private final Producer<String, String> producer;

    @Value("${kafka.servers}")
    private String servers;

    @Autowired
    private KafkaPersistService(Producer<String, String> producer) {
        this.producer = producer;
    }

    @Override
    public String name() {
        return "kafka";
    }

    @Override
    public void store(String key, String value) {
        producer.send(new ProducerRecord<>(TOPIC, key, value));
    }

    @Override
    public String fetch(String key) {
        Consumer<String, String> consumer = createConsumer();
        TopicPartition partition = new TopicPartition(TOPIC, 1);
        consumer.assign(Lists.newArrayList(partition));

        while (true) {
            final ConsumerRecords<String, String> records = consumer.poll(1000);

            if (records.isEmpty()) {
                consumer.close();
                return null;
            }

            for (ConsumerRecord<String, String> msg : records) {
                if (msg.key().equals(key)) {
                    return msg.value();
                }
            }

            consumer.commitAsync();
        }
    }

    private Consumer<String, String> createConsumer() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        return new KafkaConsumer<>(props);
    }
}
