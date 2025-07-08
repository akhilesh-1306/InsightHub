package com.project.InsightHub.embedding.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
// import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class QdrantService {
    @Value("${qdrant.host}")
    private String qdrantHost;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public void upsertChunks(List<String> texts, List<List<Double>> vectors, Long knowledgeId) throws Exception {
        // Validate inputs
        if (texts.size() != vectors.size()) {
            throw new IllegalArgumentException("Texts and vectors must have the same size");
        }
        
        // First, check if collection exists
        // try {
        //     ResponseEntity<String> collectionCheck = restTemplate.getForEntity(
        //         qdrantHost + "/collections/knowledge_chunks",
        //         String.class
        //     );
        // } catch (Exception e) {
        //     System.err.println("Collection might not exist: " + e.getMessage());
        //     // You might want to create the collection here
        // }
        
        // Build separate arrays as Qdrant expects
        List<String> ids = new ArrayList<>();
        List<Map<String, Object>> payloads = new ArrayList<>();
        
        for (int i = 0; i < texts.size(); i++) {
            String id = UUID.randomUUID().toString();
            ids.add(id);
            
            Map<String, Object> payload = Map.of(
                "text", texts.get(i),
                "knowledgeId", knowledgeId
            );
            payloads.add(payload);
        }

        // Try using the PUT endpoint instead of POST
        Map<String, Object> body = Map.of(
            "points", buildPointsArray(ids, vectors, payloads)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        String jsonBody = mapper.writeValueAsString(body);
        
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        try {
            
            // Try PUT instead of POST
            ResponseEntity<String> response = restTemplate.exchange(
                qdrantHost + "/collections/knowledge_chunks/points?wait=true",
                HttpMethod.PUT,
                request,
                String.class
            );
            
            // Check if the request was successful
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new Exception("Failed to insert points into Qdrant: " + response.getBody());
            }
            
            // Verify insertion by checking collection info
            try {
                Thread.sleep(1000); // Wait a moment for indexing
                ResponseEntity<String> collectionInfo = restTemplate.getForEntity(
                    qdrantHost + "/collections/knowledge_chunks",
                    String.class
                );
            } catch (Exception e) {
                System.err.println("Could not verify insertion: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("Error inserting points into Qdrant: " + e.getMessage());
            throw e;
        }
    }

    private List<Map<String, Object>> buildPointsArray(List<String> ids, List<List<Double>> vectors, List<Map<String, Object>> payloads) {
        List<Map<String, Object>> points = new ArrayList<>();
        
        for (int i = 0; i < ids.size(); i++) {
            Map<String, Object> point = Map.of(
                "id", ids.get(i),
                "vector", vectors.get(i),
                "payload", payloads.get(i)
            );
            points.add(point);
        }
        
        return points;
    }
}
