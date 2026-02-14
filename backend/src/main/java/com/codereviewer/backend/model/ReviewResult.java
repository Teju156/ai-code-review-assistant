package com.codereviewer.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "review_results")
public class ReviewResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repoOwner;
    private String repoName;
    private Integer prNumber;
    private String prTitle;

    @Column(columnDefinition = "TEXT")
    private String reviewComments;

    private String codeQualityScore;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }

    public ReviewResult() {}

    public ReviewResult(Long id, String repoOwner, String repoName, Integer prNumber,
                        String prTitle, String reviewComments, String codeQualityScore,
                        LocalDateTime createdAt) {
        this.id = id;
        this.repoOwner = repoOwner;
        this.repoName = repoName;
        this.prNumber = prNumber;
        this.prTitle = prTitle;
        this.reviewComments = reviewComments;
        this.codeQualityScore = codeQualityScore;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRepoOwner() { return repoOwner; }
    public void setRepoOwner(String repoOwner) { this.repoOwner = repoOwner; }

    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }

    public Integer getPrNumber() { return prNumber; }
    public void setPrNumber(Integer prNumber) { this.prNumber = prNumber; }

    public String getPrTitle() { return prTitle; }
    public void setPrTitle(String prTitle) { this.prTitle = prTitle; }

    public String getReviewComments() { return reviewComments; }
    public void setReviewComments(String reviewComments) { this.reviewComments = reviewComments; }

    public String getCodeQualityScore() { return codeQualityScore; }
    public void setCodeQualityScore(String codeQualityScore) { this.codeQualityScore = codeQualityScore; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
