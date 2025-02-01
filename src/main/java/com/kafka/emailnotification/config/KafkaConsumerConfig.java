package com.kafka.emailnotification.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {


    /**
     * Instead of Using @Value; Direct Environment variables can be used
     * to load the data to the beans
     */

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String CONSUMER_BOOTSTRAP_SERVER;

    @Value("${spring.kafka.consumer.group-id}")
    private String CONSUMER_GROUP_ID;

    @Value("${spring.kafka.consumer.properties.spring.json.trusted.packages}")
    private String JSON_TRUSTED_PACKAGES;

    @Bean
    KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerFactory){
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    ProducerFactory<String, Object> producerFactory(){
        Map<String, Object> config= new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CONSUMER_BOOTSTRAP_SERVER);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory(){
        Map<String, Object> config= new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, CONSUMER_BOOTSTRAP_SERVER);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        /**
         * In order to handle deserializer error at the run time we can not use this class
         * and we would be using ErrorHandlingDeserializer class
         * config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
         */
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_ID);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, JSON_TRUSTED_PACKAGES);
        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            KafkaTemplate<String, Object> kafkaTemplate
    ){

        /**
         *  Dead Letter Topic [DLT]
         *  It sends failed/Bad Messages to DLT
         *  The topic for DLT is same the topic in general except the extension as .DLT.
         *  For example: product-created-events-topic-dlt
         */
        DefaultErrorHandler defaultErrorHandler= new DefaultErrorHandler(
                new DeadLetterPublishingRecoverer(kafkaTemplate)
        );

        ConcurrentKafkaListenerContainerFactory<String, Object> factory=
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(defaultErrorHandler);
        return factory;
    }


}
