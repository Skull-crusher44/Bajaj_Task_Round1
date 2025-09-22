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

    /**
     * Step 1: Calls the API to generate the webhook and get the access token.
     */
    public GenerateWebhookResponse generateWebhook(GenerateWebhookRequest request) {
        System.out.println("Step 1: Generating webhook...");
        try {
            // First, let's get the raw response as String to see the actual JSON structure
            ResponseEntity<String> rawResponse = restTemplate.postForEntity(
                    GENERATE_WEBHOOK_URL,
                    request,
                    String.class
            );
            System.out.println("Raw API Response: " + rawResponse.getBody());
            
            // Now get the typed response
            ResponseEntity<GenerateWebhookResponse> response = restTemplate.postForEntity(
                    GENERATE_WEBHOOK_URL,
                    request,
                    GenerateWebhookResponse.class
            );
            System.out.println("Webhook and token received successfully!");
            System.out.println("Response Status: " + response.getStatusCode());
            
            GenerateWebhookResponse responseBody = response.getBody();
            if (responseBody != null) {
                System.out.println("Response Body - WebhookURL: " + responseBody.getWebhookURL());
                System.out.println("Response Body - AccessToken: " + (responseBody.getAccessToken() != null ? "[PRESENT]" : "[NULL]"));
            } else {
                System.out.println("Response body is null");
            }
            
            return responseBody;
        } catch (Exception e) {
            System.err.println("Error generating webhook: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Step 2: Submits the final SQL query to the received webhook URL.
     */
    public void submitSolution(String webhookUrl, String accessToken, String sqlQuery) {
        System.out.println("Step 2: Submitting solution...");

        // Set the Authorization header with the JWT Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.set("Content-Type", "application/json");

        // Create the request body
        SubmitSolutionRequest requestBody = new SubmitSolutionRequest(sqlQuery);

        // Create the HTTP entity with headers and body
        HttpEntity<SubmitSolutionRequest> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // Make the POST request to submit the solution
            ResponseEntity<String> response = restTemplate.exchange(
                    webhookUrl, // The URL from the first API call
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