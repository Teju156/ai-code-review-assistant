package com.codereviewer.backend.dto;

public class ReleaseNoteDTO {
    private String repoOwner;
    private String repoName;
    private String sinceDate;

    public String getRepoOwner() { return repoOwner; }
    public void setRepoOwner(String repoOwner) { this.repoOwner = repoOwner; }

    public String getRepoName() { return repoName; }
    public void setRepoName(String repoName) { this.repoName = repoName; }

    public String getSinceDate() { return sinceDate; }
    public void setSinceDate(String sinceDate) { this.sinceDate = sinceDate; }
}
