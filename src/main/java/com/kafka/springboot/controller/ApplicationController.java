package com.kafka.springboot.controller;

import com.kafka.springboot.dto.CreateProductRestModel;
import com.kafka.springboot.dto.ErrorMessage;
import com.kafka.springboot.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/products")
public class ApplicationController {

    private ProductService productService;

    public ApplicationController(ProductService productService){
        this.productService=productService;
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody CreateProductRestModel product){
        String productId;
        try{
            productId= productService.createProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(productId);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorMessage(new Date(), e.getMessage(), "/products"));
        }


    }
}
