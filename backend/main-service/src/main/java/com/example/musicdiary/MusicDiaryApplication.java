package com.example.musicdiary;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@RequiredArgsConstructor
@EnableDiscoveryClient
public class MusicDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicDiaryApplication.class, args);
	}

}
