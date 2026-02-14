package com.codereviewer.backend.controller;

import com.codereviewer.backend.service.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/github")
@CrossOrigin(origins = "http://localhost:3000")
public class GitHubController {

    private final GitHubService gitHubService;

    @Autowired
    public GitHubController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/pulls/{owner}/{repo}")
    public ResponseEntity<List<Map<String, Object>>> getPullRequests(
            @PathVariable String owner, @PathVariable String repo) {
        return ResponseEntity.ok(gitHubService.getOpenPullRequests(owner, repo));
    }

    @GetMapping("/pulls/{owner}/{repo}/{prNumber}/diff")
    public ResponseEntity<String> getPRDiff(
            @PathVariable String owner, @PathVariable String repo,
            @PathVariable int prNumber) {
        return ResponseEntity.ok(gitHubService.getPullRequestDiff(owner, repo, prNumber));
    }
}
