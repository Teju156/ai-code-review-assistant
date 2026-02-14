package com.codereviewer.backend.service;

import com.codereviewer.backend.dto.ReviewResponseDTO;
import com.codereviewer.backend.model.ReviewResult;
import com.codereviewer.backend.repository.ReviewResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class CodeReviewService {

    private final GitHubService gitHubService;
    private final OpenAIService openAIService;
    private final ReviewResultRepository reviewResultRepository;

    @Autowired
    public CodeReviewService(GitHubService gitHubService,
                             OpenAIService openAIService,
                             ReviewResultRepository reviewResultRepository) {
        this.gitHubService = gitHubService;
        this.openAIService = openAIService;
        this.reviewResultRepository = reviewResultRepository;
    }

    private static final String REVIEW_SYSTEM_PROMPT = """
            You are an expert senior software engineer performing a code review.
            Analyze the given pull request diff and provide:
            1. **Summary**: Brief overview of changes (2-3 sentences)
            2. **Code Quality Score**: Rate 1-10 with justification
            3. **Issues Found**: List bugs, security issues, performance problems
            4. **Suggestions**: Specific improvement recommendations
            5. **Best Practices**: Note any violated coding standards
            
            Be constructive, specific, and reference line numbers when possible.
            Format your response in Markdown.
            """;

    public ReviewResponseDTO reviewPullRequest(String owner, String repo, int prNumber) {
        Map<String, String> prDetails = gitHubService.getPullRequestDetails(owner, repo, prNumber);
        String diff = gitHubService.getPullRequestDiff(owner, repo, prNumber);

        if (diff != null && diff.length() > 12000) {
            diff = diff.substring(0, 12000) + "\n... [truncated]";
        }

        String userPrompt = String.format(
                "PR Title: %s\nPR Description: %s\n\nCode Diff:\n```\n%s\n```",
                prDetails.get("title"), prDetails.get("body"), diff
        );

        String aiReview = openAIService.chat(REVIEW_SYSTEM_PROMPT, userPrompt);

        ReviewResult result = new ReviewResult();
        result.setRepoOwner(owner);
        result.setRepoName(repo);
        result.setPrNumber(prNumber);
        result.setPrTitle(prDetails.get("title"));
        result.setReviewComments(aiReview);
        result.setCodeQualityScore(extractScore(aiReview));

        ReviewResult saved = reviewResultRepository.save(result);

        return new ReviewResponseDTO(
                saved.getId(), saved.getPrTitle(), saved.getPrNumber(),
                saved.getReviewComments(), saved.getCodeQualityScore(), saved.getCreatedAt()
        );
    }

    private String extractScore(String review) {
        if (review != null && review.contains("/10")) {
            int idx = review.indexOf("/10");
            int start = Math.max(0, idx - 2);
            return review.substring(start, idx + 3).trim();
        }
        return "N/A";
    }
}
