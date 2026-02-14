package com.codereviewer.backend.dto;

import java.time.LocalDateTime;

public class ReviewResponseDTO {
    private Long id;
    private String prTitle;
    private Integer prNumber;
    private String reviewComments;
    private String codeQualityScore;
    private LocalDateTime createdAt;

    public ReviewResponseDTO() {}

    public ReviewResponseDTO(Long id, String prTitle, Integer prNumber,
                             String reviewComments, String codeQualityScore,
                             LocalDateTime createdAt) {
        this.id = id;
        this.prTitle = prTitle;
        this.prNumber = prNumber;
        this.reviewComments = reviewComments;
        this.codeQualityScore = codeQualityScore;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPrTitle() { return prTitle; }
    public void setPrTitle(String prTitle) { this.prTitle = prTitle; }

    public Integer getPrNumber() { return prNumber; }
    public void setPrNumber(Integer prNumber) { this.prNumber = prNumber; }

    public String getReviewComments() { return reviewComments; }
    public void setReviewComments(String reviewComments) { this.reviewComments = reviewComments; }

    public String getCodeQualityScore() { return codeQualityScore; }
    public void setCodeQualityScore(String codeQualityScore) { this.codeQualityScore = codeQualityScore; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
