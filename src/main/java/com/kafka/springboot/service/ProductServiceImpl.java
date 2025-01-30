package com.kafka.springboot.service;

import com.event.kafka.product.ProductCreatedEvent;
import com.kafka.springboot.dto.CreateProductRestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService{

    public final Logger logger= LoggerFactory.getLogger(this.getClass());

    private KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public  ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    @Override
    public String createProduct(CreateProductRestModel productResModel) throws Exception{
        String productId= UUID.randomUUID().toString();
        logger.info("ProductId {}",productId);

        ProductCreatedEvent productCreatedEvent= new ProductCreatedEvent(
                productId,
                productResModel.getTitle(),
                productResModel.getPrice(),
                productResModel.getQuantity()
        );

        /**
         *  Using Synchronous communication
         */
        SendResult<String, ProductCreatedEvent> result = kafkaTemplate.send(
                "product-created-events-topic", productId, productCreatedEvent
        ).get();

        logger.info("Submitted to Topic {}", result.getRecordMetadata().topic());
        logger.info("Partitions {}", result.getRecordMetadata().partition());
        logger.info("Offset {}", result.getRecordMetadata().offset());

        return productId;

        /**
         *  This commented out section can be used for Asynchronous communication
         */
//        CompletableFuture<SendResult<String, ProductCreatedEvent>> future=
//                kafkaTemplate.send(
//                        "product-created-events-topic", productId, productCreatedEvent
//                );
//
//        future.whenComplete((result, exception)->{
//            if(exception!=null){
//                logger.info("Error Occurred Failed to send Message: "+exception.getMessage());
//            }else {
//                logger.info("Message Sent! "+result.getRecordMetadata());
//            }
//        });
//
//        future.join();

        /**
         * Asynchronous communication ends here.
         */
    }
}
