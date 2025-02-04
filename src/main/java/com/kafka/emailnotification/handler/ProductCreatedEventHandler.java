 package com.kafka.emailnotification.handler;

 import com.event.kafka.product.ProductCreatedEvent;
 import com.kafka.emailnotification.exception.NonRetryableException;
 import com.kafka.emailnotification.exception.RetryableException;
 import org.apache.kafka.common.errors.ResourceNotFoundException;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.http.HttpMethod;
 import org.springframework.http.ResponseEntity;
 import org.springframework.kafka.annotation.KafkaHandler;
 import org.springframework.kafka.annotation.KafkaListener;
 import org.springframework.stereotype.Component;
 import org.springframework.web.client.HttpServerErrorException;
 import org.springframework.web.client.RestTemplate;

 @Component
 @KafkaListener(topics = "product-created-events-topic")
 public class ProductCreatedEventHandler {
     private final Logger logger= LoggerFactory.getLogger(this.getClass());

     private RestTemplate restTemplate;
     public ProductCreatedEventHandler(RestTemplate restTemplate){
         this.restTemplate=restTemplate;
     }

     @KafkaHandler
     public void handle(ProductCreatedEvent productCreatedEvent){
         logger.info("Event Received {}", productCreatedEvent.getTitle());

         String requestURL="http://localhost:8082/api/response/200";
         ResponseEntity<String> response=null;
         try{
             response=restTemplate.exchange(requestURL, HttpMethod.GET, null, String.class);
         }catch (ResourceNotFoundException ex){
             logger.info("Resource Not Available {}", ex.getMessage());
             throw new RetryableException(ex);
         }catch (HttpServerErrorException ex){
             logger.info("Server Not Available {}",ex.getMessage());
             throw new NonRetryableException(ex);
         }catch (Exception ex){
             logger.info("Exception Occurred in service {}", ex.getMessage());
             throw new NonRetryableException(ex);
         }
         logger.info("Response Received {}", response.getBody());

     }
 }
