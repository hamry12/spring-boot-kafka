package com.kafka.springboot.service;

import com.kafka.springboot.dto.CreateProductRestModel;

public interface ProductService {
    String createProduct(CreateProductRestModel productResModel) throws Exception;
}
