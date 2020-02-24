package com.elevenst.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author hrjin
 * @version 1.0
 * @since 2020-02-24
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
     public static void main(String[] args) {
         SpringApplication.run(EurekaServerApplication.class, args);
     }
}
