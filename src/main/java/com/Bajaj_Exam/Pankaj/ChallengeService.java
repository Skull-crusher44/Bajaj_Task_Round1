package com.Bajaj_Exam.Pankaj;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.Bajaj_Exam.Pankaj.dto.GenerateWebhookRequest;
import com.Bajaj_Exam.Pankaj.dto.GenerateWebhookResponse;
import com.Bajaj_Exam.Pankaj.dto.SubmitSolutionRequest;

@Service
public class ChallengeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String GENERATE_WEBHOOK_URL = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

    public GenerateWebhookResponse generateWebhook(GenerateWebhookRequest request) {
        System.out.println("Step 1: Generating webhook...");
        try {
            ResponseEntity<GenerateWebhookResponse> response = restTemplate.postForEntity(
                    GENERATE_WEBHOOK_URL,
                    request,
                    GenerateWebhookResponse.class
            );
            System.out.println("Webhook and token received successfully!");
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Error generating webhook: " + e.getMessage());
            return null;
        }
    }

    public void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        System.out.println("Step 2: Submitting solution...");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", accessToken);
        headers.set("Content-Type", "application/json");

        SubmitSolutionRequest requestBody = new SubmitSolutionRequest(sqlQuery);
        HttpEntity<SubmitSolutionRequest> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    webhookUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );
            System.out.println("Solution submitted successfully!");
            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
        } catch (Exception e) {
            System.err.println("Error submitting solution: " + e.getMessage());
        }
    }
}