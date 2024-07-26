package com.web10.taskmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.web10.taskmanagement.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class TaskTrackingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskTrackingBackendApplication.class, args);
	}

}
