package com.bookkeeping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author bookkeeping
 */
@EnableScheduling
@SpringBootApplication
public class BookkeepingServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookkeepingServerApplication.class, args);
    }

}
