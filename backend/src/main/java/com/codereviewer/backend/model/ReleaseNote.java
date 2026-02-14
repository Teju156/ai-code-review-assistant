package com.codereviewer.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "release_notes")
public class ReleaseNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String repoOwner;
    private String repoName;
    private String version;

    @Column(columnDefinition = "TEXT")
    private String features;

    @Column(columnDefinition = "TEXT")
    private String improvements;

    @Column(columnDefinition = "TEXT")
    private String bugFixes;

    @Column(columnDefinition = "TEXT")
    private String rawCommits;

    private LocalDateTime generatedAt;

    @PrePersist
    protected void onCreate() { this.generatedAt = LocalDateTime.now(); }

    public ReleaseNote() {}

    public ReleaseNote(Long id, String repoOwner, String repoName, String version,
                       String features, String improvements, String bugFixes,
                       String rawCommits, LocalDateTime generatedAt) {
        this.id = id;
        this.repoOwner = repoOwner;
        this.repoName = repoName;
        this.version = version;
        this.features = features;
        this.improvements = improvements;
        this.bugFixes = bugFixes;
        this.rawCommits = rawCommits;
        this.generatedAt = generatedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRepoOwner() { return repoOwner; }
    public void setRepoOwner(String repoOwner) { this.repoOwner = repoOwner; }

    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }

    public String getImprovements() { return improvements; }
    public void setImprovements(String improvements) { this.improvements = improvements; }

    public String getBugFixes() { return bugFixes; }
    public void setBugFixes(String bugFixes) { this.bugFixes = bugFixes; }

    public String getRawCommits() { return rawCommits; }
    public void setRawCommits(String rawCommits) { this.rawCommits = rawCommits; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}
