package com.elevenst.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author hrjin
 * @version 1.0
 * @since 2020-02-10
 */
@Service
public class ProductRemoteServiceImpl implements ProductRemoteService {

    public static final String URL = "http://localhost:8082/products/";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getProductInfo(String productId) {
        return restTemplate.getForObject(URL + productId, String.class);
    }
}
