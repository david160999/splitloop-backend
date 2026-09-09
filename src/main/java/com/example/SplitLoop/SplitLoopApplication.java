package com.example.SplitLoop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SplitLoopApplication {

	public static void main(String[] args) {
		SpringApplication.run(SplitLoopApplication.class, args);
	}

}
