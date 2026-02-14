package com.codereviewer.backend.controller;

import com.codereviewer.backend.dto.ReviewRequestDTO;
import com.codereviewer.backend.dto.ReviewResponseDTO;
import com.codereviewer.backend.model.ReviewResult;
import com.codereviewer.backend.repository.ReviewResultRepository;
import com.codereviewer.backend.service.CodeReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {

    private final CodeReviewService codeReviewService;
    private final ReviewResultRepository reviewResultRepository;

    @Autowired
    public ReviewController(CodeReviewService codeReviewService,
                            ReviewResultRepository reviewResultRepository) {
        this.codeReviewService = codeReviewService;
        this.reviewResultRepository = reviewResultRepository;
    }

    @PostMapping("/analyze")
    public ResponseEntity<ReviewResponseDTO> analyzePR(@RequestBody ReviewRequestDTO request) {
        ReviewResponseDTO response = codeReviewService.reviewPullRequest(
                request.getRepoOwner(), request.getRepoName(), request.getPrNumber()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history/{owner}/{repo}")
    public ResponseEntity<List<ReviewResult>> getReviewHistory(
            @PathVariable String owner, @PathVariable String repo) {
        return ResponseEntity.ok(
                reviewResultRepository.findByRepoOwnerAndRepoNameOrderByCreatedAtDesc(owner, repo)
        );
    }
}
