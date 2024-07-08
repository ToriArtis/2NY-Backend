package com.mega._NY.auth.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * 프롬프트 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatGPTDTO {

    private String model;

    private List<Map<String, String>> messages;
    private float temperature;

    @Builder
    ChatGPTDTO(String model, List<Map<String, String>> messages, float temperature) {
        this.model = model;
        this.messages = messages;
        this.temperature = temperature;
    }

}