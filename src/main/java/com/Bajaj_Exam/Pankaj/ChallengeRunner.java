package com.Bajaj_Exam.Pankaj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.Bajaj_Exam.Pankaj.dto.GenerateWebhookRequest;
import com.Bajaj_Exam.Pankaj.dto.GenerateWebhookResponse;

@Component
public class ChallengeRunner implements CommandLineRunner {

    @Autowired
    private ChallengeService challengeService;

    // Injecting values from application.properties
    @Value("${user.name}")
    private String name;

    @Value("${user.regNo}")
    private String regNo;

    @Value("${user.email}")
    private String email;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Challenge started!");

        // 1. Prepare and send the initial request to get the webhook
        GenerateWebhookRequest initialRequest = new GenerateWebhookRequest(name, regNo, email);
        GenerateWebhookResponse webhookResponse = challengeService.generateWebhook(initialRequest);

        // Debug logging
        if (webhookResponse != null) {
            System.out.println("Webhook Response received:");
            System.out.println("- Webhook URL: " + webhookResponse.getWebhookURL());
            System.out.println("- Access Token: " + (webhookResponse.getAccessToken() != null ? "[PRESENT]" : "[NULL]"));
        } else {
            System.out.println("Webhook Response is null");
        }

        if (webhookResponse != null && webhookResponse.getWebhookURL() != null && !webhookResponse.getWebhookURL().trim().isEmpty()) {
            // 2. Define the final SQL query for Question 1
            String sqlQuery = "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, d.DEPARTMENT_NAME FROM PAYMENTS p JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID WHERE DAY(p.PAYMENT_TIME) <> 1 ORDER BY p.AMOUNT DESC LIMIT 1;";

            // 3. Submit the solution
            challengeService.submitSolution(
                    webhookResponse.getWebhookURL(),
                    webhookResponse.getAccessToken(),
                    sqlQuery
            );
        } else {
            System.err.println("Could not proceed without a webhook URL. Aborting.");
            if (webhookResponse == null) {
                System.err.println("Reason: Webhook response is null");
            } else if (webhookResponse.getWebhookURL() == null) {
                System.err.println("Reason: Webhook URL is null");
            } else if (webhookResponse.getWebhookURL().trim().isEmpty()) {
                System.err.println("Reason: Webhook URL is empty");
            }
        }

        System.out.println("Challenge finished!");
    }
}