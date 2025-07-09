package com.project.InsightHub.chat.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.InsightHub.embedding.service.EmbeddingService;
import com.project.InsightHub.embedding.service.QdrantService;

@Service
public class ChatService {
    @Autowired private EmbeddingService embeddingService;
    @Autowired private QdrantService qdrantService;

    // @Value("${openai.api.key}")
    // private String openAiKey;

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public String ask(String question, Long workspaceId) throws Exception {
        // 1. Embed the question
        List<List<Double>> vectors = embeddingService.embedChunks(List.of(question));
        List<Double> questionVector = vectors.get(0);

        // 2. Search Qdrant for similar chunks
        List<String> topChunks = qdrantService.searchSimilarChunks(questionVector, workspaceId);

        // 3. Combine context and send to Chat API
        String context = String.join("\n---\n", topChunks);
        String prompt = "Answer the question using only the context below.\n\n"
                      + "Context:\n" + context + "\n\n"
                      + "Question: " + question;

        // return callOpenAi(prompt);
        return callGemini(prompt);
        // return "";
    }

    private String callGemini(String prompt) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-goog-api-key", geminiApiKey);  
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
            "contents", List.of(
                Map.of("parts", List.of(
                    Map.of("text", prompt)
                ))
            )
        );

        HttpEntity<String> request = new HttpEntity<>(new ObjectMapper().writeValueAsString(body), headers);
        ResponseEntity<String> response = new RestTemplate().postForEntity(
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent",
            request, String.class
        );

        JsonNode json = new ObjectMapper().readTree(response.getBody());
        return json.get("candidates").get(0).get("content").get("parts").get(0).get("text").asText();
    }

    /*
     * If using OpenAI api ------
     */
    // private String callOpenAi(String prompt) throws Exception {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setBearerAuth(openAiKey);
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     Map<String, Object> body = Map.of(
    //         "model", "gpt-3.5-turbo",
    //         "messages", List.of(
    //             Map.of("role", "user", "content", prompt)
    //         )
    //     );

    //     HttpEntity<String> request = new HttpEntity<>(new ObjectMapper().writeValueAsString(body), headers);
    //     ResponseEntity<String> response = new RestTemplate().postForEntity(
    //         "https://api.openai.com/v1/chat/completions", request, String.class
    //     );

    //     JsonNode json = new ObjectMapper().readTree(response.getBody());
    //     return json.get("choices").get(0).get("message").get("content").asText();
    // }


}
