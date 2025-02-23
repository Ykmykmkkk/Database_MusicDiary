package com.example.musicdiary;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class MusicDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicDiaryApplication.class, args);
	}

}
