package com.elevenst.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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

    /**
     * Hystrix 에서 fallback 의 실행 여부는 Exception 이 발생했는가...
     *
     * @param productId
     * @return
     */
    @Override
    @HystrixCommand(commandKey = "productInfo", fallbackMethod = "getProductInfoFallback")
    public String getProductInfo(String productId) {
        return restTemplate.getForObject(URL + productId, String.class);
    }

    // Throwable 파라미터로 Fallback 원인을 알 수 있다.
    public String getProductInfoFallback(String productId, Throwable t) {
        System.out.println("t = " + t);
        return "[This product is sold out!!!]";
    }
}
