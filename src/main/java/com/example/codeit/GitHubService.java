package com.example.codeit;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubService {
    private static final String GITHUB_API_BASE = "https://api.github.com/repos/";
    private static final String GITHUB_TOKEN = ""; 

    public void createWorkflow(String repoUrl) throws Exception {
    String workflowContent = "name: Run Tests\n" +
    "on: [push, pull_request]\n" +
    "jobs:\n" +
    "  build:\n" +
    "    runs-on: ubuntu-latest\n" +
    "    steps:\n" +
    "      - name: Checkout code\n" +
    "        uses: actions/checkout@v2\n" +
    "      - name: Grant execute permissions for gradlew\n" + 
    "        run: chmod +x ./gradlew\n" + 
    "      - name: Run Tests\n" +
    "        run: ./gradlew test";


    String encodedContent = Base64.getEncoder().encodeToString(workflowContent.getBytes(StandardCharsets.UTF_8));
    String apiUrl = GITHUB_API_BASE + repoUrl + "/contents/.github/workflows/test.yml";

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Authorization", "Bearer " + GITHUB_TOKEN);

    // Check if file already exists to fetch its SHA
    String sha = null;
    try {
        ResponseEntity<Map> response = restTemplate.getForEntity(apiUrl, Map.class);
        sha = (String) response.getBody().get("sha"); // Get SHA if file exists
    } catch (Exception e) {
       
    }
    Map<String, Object> body = new HashMap<>();
    body.put("message", "Add GitHub Actions workflow for testing");
    body.put("content", encodedContent);
    if (sha != null) {
        body.put("sha", sha); 
    }

    HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
    restTemplate.put(apiUrl, request);
}

     public void createWebhook(String repoUrl) throws Exception {
        String webhookUrl = "http://localhost:8080/webhook/github";
        String apiUrl = GITHUB_API_BASE + repoUrl + "/hooks";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + GITHUB_TOKEN);

        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            JSONArray hooks = new JSONArray(response.getBody());
            for (int i = 0; i < hooks.length(); i++) {
                JSONObject hook = hooks.getJSONObject(i);
                String existingUrl = hook.getJSONObject("config").getString("url");
                if (webhookUrl.equals(existingUrl)) {
                    System.out.println("Webhook already exists: " + existingUrl);
                    return;
                }
            }
        }

        Map<String, Object> config = new HashMap<>();
        config.put("url", webhookUrl);
        config.put("content_type", "json");

        Map<String, Object> body = new HashMap<>();
        body.put("name", "web");
        body.put("active", true);
        body.put("events", List.of("workflow_run"));
        body.put("config", config);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(apiUrl, request, String.class);
    }
}
