package com.kafka.springboot.config;



import com.event.kafka.product.ProductCreatedEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String BOOTSTRAP_SERVERS;

    @Value("${spring.kafka.producer.key-serializer}")
    private String KEY_SERIALIZER;

    @Value("${spring.kafka.producer.value-serializer}")
    private String VALUE_SERIALIZER;

    @Value("${spring.kafka.producer.acks}")
    private String ACKS;

    @Value("${spring.kafka.producer.properties.delivery.timeout.ms}")
    private String DELIVERY_TIMEOUT_MS;

    @Value("${spring.kafka.producer.properties.linger.ms}")
    private String LINGER_MS;

    @Value("${spring.kafka.producer.properties.request.timeout.ms}")
    private String REQUEST_TIMEOUT_MS;

    @Value("${spring.kafka.producer.properties.max.in.flight.requests.per.connection}")
    private Integer MAX_IN_FLIGHT_REQUEST;

    @Value("${spring.kafka.producer.properties.enable.idempotence}")
    private boolean IDEMPOTENT_CONFIG;


    Map<String, Object> producerConfigs(){
        Map<String, Object> config= new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KEY_SERIALIZER);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER);
        config.put(ProducerConfig.ACKS_CONFIG, ACKS);
        config.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, DELIVERY_TIMEOUT_MS);
        config.put(ProducerConfig.LINGER_MS_CONFIG, LINGER_MS);
        config.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, REQUEST_TIMEOUT_MS);
        config.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, IDEMPOTENT_CONFIG);
        config.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, MAX_IN_FLIGHT_REQUEST);
        return config;
    }

    @Bean
    ProducerFactory<String, ProductCreatedEvent> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    NewTopic createTopic(){
        return TopicBuilder
                .name("product-created-events-topic")
                .partitions(3)
                .replicas(3)
                .configs(
                        Map.of("min.insync.replicas", "2")
                )
                .build();
    }
}
