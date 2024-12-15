package com.example.codeit;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.example.codeit.model.projetModel;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CodeitApplicationTests {
	@Autowired
    private TestRestTemplate restTemplate;
	 @MockBean
    private GitHubService gitHubService;
	@InjectMocks
    private ProjectController projectController;

	@Test
	void contextLoads() {
	}
	 @Test
	void testCreateGitHubWorkflow() throws Exception {
		 String repoUrl = "vijay78274/ProChatver1.git"; 
		 gitHubService.createWorkflow(repoUrl);
		 gitHubService.createWebhook(repoUrl);
	 }
     @Test  
     void projectTest(){
        projetModel projectSubmission = new projetModel();
        projectSubmission.setRepoUrl("vijay78274/ProChatver1.git");
     }
	@Test
    void testSubmitProject() throws Exception {
        projetModel projectSubmission = new projetModel();
        projectSubmission.setRepoUrl("vijay78274/ProChatver1.git");
        doNothing().when(gitHubService).createWorkflow(anyString());
        doNothing().when(gitHubService).createWebhook(anyString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<projetModel> entity = new HttpEntity<>(projectSubmission, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "/api/projects/submit", HttpMethod.POST, entity, String.class);

        assert response.getStatusCode().is2xxSuccessful();
        assert response.getBody().equals("Workflow and webhook created. Testing initiated.");

        verify(gitHubService, times(1)).createWorkflow(projectSubmission.getRepoUrl());
        verify(gitHubService, times(1)).createWebhook(projectSubmission.getRepoUrl());
    }
}
