package com.elevenst.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hrjin
 * @version 1.0
 * @since 2020-02-10
 */
@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping("/{proudctId}")
    public String getProduct(@PathVariable String proudctId) {
        // 1. throw exception 일 경우
        throw new RuntimeException("I/O Exception~~");

        // 2. Thread
        // Thread 로 timeout 설정
//        try {
//            Thread.sleep(2000);
//        }catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Called proudct id : " + proudctId);
//        return "[product id = " + proudctId + " at " + System.currentTimeMillis() + "]";
    }
}
