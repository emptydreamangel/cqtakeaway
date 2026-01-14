package com.blue.jitian;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@MapperScan("com.blue.jitian.Mapper")
@Slf4j
@SpringBootApplication
public class JitianApplication {

    public static void main(String[] args) {
        SpringApplication.run(JitianApplication.class, args);
        log.info("server start");
    }

}
