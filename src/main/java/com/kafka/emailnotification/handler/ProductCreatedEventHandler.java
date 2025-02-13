 package com.kafka.emailnotification.handler;

 import com.event.kafka.product.ProductCreatedEvent;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.kafka.annotation.KafkaHandler;
 import org.springframework.kafka.annotation.KafkaListener;
 import org.springframework.stereotype.Component;

 @Component
 @KafkaListener(topics = "product-created-events-topic")
 public class ProductCreatedEventHandler {

     private final Logger logger= LoggerFactory.getLogger(this.getClass());

     @KafkaHandler
     public void handle(ProductCreatedEvent productCreatedEvent){
         logger.info("Event Received {}", productCreatedEvent.getTitle());
     }
 }
