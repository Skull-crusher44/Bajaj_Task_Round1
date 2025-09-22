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

    @Value("${user.name}")
    private String name;

    @Value("${user.regNo}")
    private String regNo;

    @Value("${user.email}")
    private String email;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Challenge started!");

        GenerateWebhookRequest initialRequest = new GenerateWebhookRequest(name, regNo, email);
        GenerateWebhookResponse webhookResponse = challengeService.generateWebhook(initialRequest);

        if (webhookResponse != null && webhookResponse.getWebhookURL() != null && !webhookResponse.getWebhookURL().trim().isEmpty()) {
            String sqlQuery = "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, d.DEPARTMENT_NAME FROM PAYMENTS p JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID WHERE DAY(p.PAYMENT_TIME) <> 1 ORDER BY p.AMOUNT DESC LIMIT 1;";

            challengeService.submitSolution(
                    webhookResponse.getWebhookURL(),
                    webhookResponse.getAccessToken(),
                    sqlQuery
            );
        } else {
            System.err.println("Could not proceed without a webhook URL. Aborting.");
        }

        System.out.println("Challenge finished!");
    }
}