package com.example.codeit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebHookController {

    
    @PostMapping("/github")
    public ResponseEntity<String> handleGitHubWebhook(@RequestBody Map<String, Object> payload) {
        @SuppressWarnings("unchecked")
        Map<String, Object> workflowRun = (Map<String, Object>) payload.get("workflow_run");
        String status = (String) workflowRun.get("status");     
        String conclusion = (String) workflowRun.get("conclusion"); 
        
        System.out.println("Test status: " + status + ", conclusion: " + conclusion);

        return ResponseEntity.ok("Webhook processed");
    }
}

