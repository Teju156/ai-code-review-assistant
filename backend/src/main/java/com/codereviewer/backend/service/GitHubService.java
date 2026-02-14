package com.codereviewer.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;

@Service
public class GitHubService {

    private final WebClient webClient;

    public GitHubService(@Value("${github.api.base-url}") String baseUrl,
                         @Value("${github.api.token}") String token) {

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(16 * 1024 * 1024))
                .build();

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .exchangeStrategies(strategies)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .build();
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getOpenPullRequests(String owner, String repo) {
        List<Map<String, Object>> response = webClient.get()
                .uri("/repos/{owner}/{repo}/pulls?state=open&per_page=20", owner, repo)
                .retrieve()
                .bodyToMono(List.class)
                .block();

        List<Map<String, Object>> prs = new ArrayList<>();
        if (response != null) {
            for (Map<String, Object> pr : response) {
                Map<String, Object> prData = new HashMap<>();
                prData.put("number", pr.get("number"));
                prData.put("title", pr.get("title"));
                Map<String, Object> user = (Map<String, Object>) pr.get("user");
                prData.put("user", user != null ? user.get("login") : "unknown");
                prData.put("created_at", pr.get("created_at"));
                prData.put("html_url", pr.get("html_url"));
                prs.add(prData);
            }
        }
        return prs;
    }

    public String getPullRequestDiff(String owner, String repo, int prNumber) {
        return webClient.get()
                .uri("/repos/{owner}/{repo}/pulls/{number}", owner, repo, prNumber)
                .header(HttpHeaders.ACCEPT, "application/vnd.github.v3.diff")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getPullRequestDetails(String owner, String repo, int prNumber) {
        Map<String, Object> pr = webClient.get()
                .uri("/repos/{owner}/{repo}/pulls/{number}", owner, repo, prNumber)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, String> details = new HashMap<>();
        if (pr != null) {
            details.put("title", String.valueOf(pr.get("title")));
            details.put("body", pr.get("body") != null ? String.valueOf(pr.get("body")) : "No description");
            Map<String, Object> user = (Map<String, Object>) pr.get("user");
            details.put("user", user != null ? String.valueOf(user.get("login")) : "unknown");
        }
        return details;
    }

    @SuppressWarnings("unchecked")
    public List<String> getCommitsSince(String owner, String repo, String sinceDate) {
        List<Map<String, Object>> response = webClient.get()
                .uri("/repos/{owner}/{repo}/commits?since={since}&per_page=50", owner, repo, sinceDate)
                .retrieve()
                .bodyToMono(List.class)
                .block();

        List<String> commitMessages = new ArrayList<>();
        if (response != null) {
            for (Map<String, Object> commitWrapper : response) {
                Map<String, Object> commit = (Map<String, Object>) commitWrapper.get("commit");
                if (commit != null) {
                    commitMessages.add(String.valueOf(commit.get("message")));
                }
            }
        }
        return commitMessages;
    }
}
