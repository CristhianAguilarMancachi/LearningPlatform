package com.ucb.plataforma.res;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ucb.plataforma.res", "com.ucb.plataforma.reviews"})
public class MsResApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsResApplication.class, args);
	}

}
