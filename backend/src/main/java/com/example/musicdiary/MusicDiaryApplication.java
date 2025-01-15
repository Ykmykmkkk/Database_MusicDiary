package com.example.musicdiary;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MusicDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicDiaryApplication.class, args);
	}

}
