package com.example.musicdiary.common.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // 필요하면 매핑 설정 (선택적)
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)  // 필드 매칭 활성화
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);  // private 필드 매핑 허용
        return modelMapper;
    }
}
