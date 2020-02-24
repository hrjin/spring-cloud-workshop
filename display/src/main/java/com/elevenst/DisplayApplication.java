package com.elevenst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

// Hystrix를 통해 실행되는 모든 메소드는 정해진 응답시간 내에 반환돼야 한다.
// 그렇지 못한 경우, Exception이 발생하며, Fallback이 정의된 경우 수행된다.
// Timeout 시간은 조절할 수 있다. (default = 1000s)
@EnableCircuitBreaker
@EnableEurekaClient
@SpringBootApplication
public class DisplayApplication {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(DisplayApplication.class);
    }

}
