package com.project.InsightHub.embedding.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmbeddingService {
    // @Value("${openai.api.key}")
    // private String openAiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    private static final int CHUNK_SIZE = 500;

    public List<String> splitText(String text) {
        List<String> chunks = new ArrayList<>();
        int start = 0;
        while (start < text.length()) {
            int end = Math.min(start + CHUNK_SIZE, text.length());
            chunks.add(text.substring(start, end));
            start = end;
        }
        return chunks;
    }

    public List<List<Double>> embedChunks(List<String> chunks) throws Exception {
        /*
         * This part of the code can be used if using OpenAI api.
         * I apparently dont have the credits for it, so using another model which is running in the embedder dir
         * If have the api key, just add in application.properties and uncomment the following code
         * -----------------------Starting---------------------------
         */
        // List<List<Double>> vectors = new ArrayList<>();

        // for (String chunk : chunks) {
        //     HttpHeaders headers = new HttpHeaders();
        //     headers.setBearerAuth(openAiKey);
        //     headers.setContentType(MediaType.APPLICATION_JSON);

        //     Map<String, Object> body = Map.of(
        //         "model", "text-embedding-3-small",
        //         "input", chunk
        //     );

        //     HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(body), headers);
        //     ResponseEntity<String> response = restTemplate.postForEntity(
        //         "https://api.openai.com/v1/embeddings", request, String.class
        //     );

        //     JsonNode node = mapper.readTree(response.getBody());
        //     JsonNode embedding = node.get("data").get(0).get("embedding");

        //     List<Double> vector = new ArrayList<>();
        //     for (JsonNode val : embedding) {
        //         vector.add(val.asDouble());
        //     }
        //     vectors.add(vector);
        // }

        // return vectors;

        /*
         * -----------------------Ending---------------------------
         */

        /*
         * This part of the code can be used if using Huggingface api.
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of("texts", chunks);
        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(body), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
            "http://localhost:5005/embed", request, String.class
        );

        JsonNode embeddingArray = mapper.readTree(response.getBody()).get("embeddings");

        List<List<Double>> vectors = new ArrayList<>();
        for (JsonNode vectorNode : embeddingArray) {
            List<Double> vector = new ArrayList<>();
            for (JsonNode val : vectorNode) {
                vector.add(val.asDouble());
            }
            vectors.add(vector);
        }

        return vectors;
    }
}
