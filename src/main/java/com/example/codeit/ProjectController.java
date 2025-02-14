package com.example.codeit;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.codeit.model.projetModel;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final GitHubService gitHubService;
    public ProjectController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitProject(@RequestBody projetModel submission) {
        try {
            String repoUrl = submission.getRepoUrl();
            gitHubService.createWorkflow(repoUrl);  
            gitHubService.createWebhook(repoUrl);   

            return ResponseEntity.ok("Workflow and webhook created. Testing initiated.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
    private static final String[] str = {"U", "p","ay"};


    @PostMapping("/checkSubstring")
    public ResponseEntity<Map<String, Boolean>> checkSubstring(@RequestBody String input) {
    Map<String, Boolean> result = new HashMap<>();
    
    // Check predefined substrings
    boolean containsSubString1 = input.contains(str[0]);
    boolean containsSubString2 = input.contains(str[1]);
    boolean containsSubString3 = input.contains(str[2]);

    // Store results in a map
    result.put("substring1", containsSubString1);
    result.put("substring2", containsSubString2);
    result.put("substring3", containsSubString3);

    // Return result as a JSON object
    return ResponseEntity.ok(result);
    }

}

