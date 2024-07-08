package com.mega._NY.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mega._NY.auth.config.ChatGPTConfig;
import com.mega._NY.auth.dto.ChatGPTDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ChatGPT Service Implementation
 *
 * Provides methods to interact with OpenAI's ChatGPT models.
 *
 * @author
 * @since 12/29/23
 */
@Log4j2
@Service
public class ChatGPTService {

    private final ChatGPTConfig chatGPTConfig;

    public ChatGPTService(ChatGPTConfig chatGPTConfig) {
        this.chatGPTConfig = chatGPTConfig;
    }

    @Value("${openai.model}")
    private String model;

    /**
     * Fetches the list of available models.
     *
     * @return List of models.
     */
    public List<Map<String, Object>> modelList() {
        log.debug("[+] Fetching model list.");
        List<Map<String, Object>> resultList = null;

        // Get headers with token information
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // Make REST call to OpenAI API
        ResponseEntity<String> response = chatGPTConfig.restTemplate()
                .exchange(
                        "https://api.openai.com/v1/models",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class
                );

        try {
            // Parse response using Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> data = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
            resultList = (List<Map<String, Object>>) data.get("data");
            resultList.forEach(object -> {
                log.debug("ID: " + object.get("id"));
                log.debug("Object: " + object.get("object"));
                log.debug("Created: " + object.get("created"));
                log.debug("Owned By: " + object.get("owned_by"));
            });
        } catch (JsonProcessingException e) {
            log.debug("JsonProcessingException :: " + e.getMessage());
        }

        return resultList;
    }

    /**
     * Checks if the specified model is valid.
     *
     * @param modelName Model name.
     * @return Model details.
     */
    public Map<String, Object> isValidModel(String modelName) {
        log.debug("[+] Checking if model is valid: " + modelName);
        Map<String, Object> result = new HashMap<>();

        // Get headers with token information
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // Make REST call to OpenAI API
        ResponseEntity<String> response = chatGPTConfig.restTemplate()
                .exchange(
                        "https://api.openai.com/v1/models/" + modelName,
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class
                );

        try {
            // Parse response using Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public Map<String, Object> prompt(ChatGPTDTO chatGPTDTO) {
        log.debug("[+] Executing prompt.");

        Map<String, Object> result = new HashMap<>();

        // Get headers with token information
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // Prepare request body
        chatGPTDTO.setModel(model);
        String requestBody;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            requestBody = objectMapper.writeValueAsString(chatGPTDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Make REST call to OpenAI API
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = chatGPTConfig.restTemplate()
                .exchange(
                        "https://api.openai.com/v1/chat/completions",
                        HttpMethod.POST,
                        requestEntity,
                        String.class
                );

        try {
            // Parse response using Jackson
            result = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
