package com.elevenst.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hrjin
 * @version 1.0
 * @since 2020-02-25
 */
// 1) Eureka 사용 시 URL(http://localhost:8082/) 제거
// 2) fallback 속성으로 Feign용 Hystrix Fallback 명시
@FeignClient(name = "product", fallbackFactory = FeignProductRemoteServiceFallbackFactory.class)
public interface FeignProductRemoteService {

    @RequestMapping("/products/{productId}")
    String getProductInfo(@PathVariable(name = "productId") String productId);
}
