package com.Bajaj_Exam.Pankaj.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenerateWebhookResponse {
    @JsonProperty("webhook")
    private String webhookURL;
    private String accessToken;
}