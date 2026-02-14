package com.codereviewer.backend.service;

import com.codereviewer.backend.model.ReleaseNote;
import com.codereviewer.backend.repository.ReleaseNoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReleaseNotesService {

    private final GitHubService gitHubService;
    private final OpenAIService openAIService;
    private final ReleaseNoteRepository releaseNoteRepository;

    @Autowired
    public ReleaseNotesService(GitHubService gitHubService,
                               OpenAIService openAIService,
                               ReleaseNoteRepository releaseNoteRepository) {
        this.gitHubService = gitHubService;
        this.openAIService = openAIService;
        this.releaseNoteRepository = releaseNoteRepository;
    }

    private static final String RELEASE_NOTES_PROMPT = """
            You are a technical writer generating release notes from commit messages.
            Categorize commits into exactly these sections:

            ## Features
            - New functionality additions

            ## Improvements
            - Enhancements, performance optimizations, refactors

            ## Bug Fixes
            - Resolved issues and stability improvements

            Rules:
            - Skip dependency updates, merge commits, and CI/CD changes
            - Translate technical commit messages into user-friendly language
            - Each item should be one clear sentence
            - Format in Markdown
            """;

    public ReleaseNote generateReleaseNotes(String owner, String repo, String sinceDate) {
        // 1. Fetch commits from GitHub
        List<String> commits = gitHubService.getCommitsSince(owner, repo, sinceDate);
        String rawCommits = String.join("\n", commits);

        String features = "No data";
        String improvements = "No data";
        String bugFixes = "No data";

        // 2. Try AI, fallback to raw commits if it fails
        try {
            String userPrompt = "Generate release notes from these commits:\n\n" + rawCommits;
            String aiResponse = openAIService.chat(RELEASE_NOTES_PROMPT, userPrompt);

            if (aiResponse != null && !aiResponse.startsWith("Error")) {
                features = extractSection(aiResponse, "Features");
                improvements = extractSection(aiResponse, "Improvements");
                bugFixes = extractSection(aiResponse, "Bug Fixes");
            } else {
                // Fallback: show raw commits
                features = "AI unavailable. Raw commits:\n" + rawCommits;
                improvements = "N/A";
                bugFixes = "N/A";
            }
        } catch (Exception e) {
            features = "AI unavailable. Raw commits:\n" + rawCommits;
            improvements = "N/A";
            bugFixes = "N/A";
        }

        // 3. Save and return
        ReleaseNote note = new ReleaseNote();
        note.setRepoOwner(owner);
        note.setRepoName(repo);
        note.setVersion("auto-" + sinceDate);
        note.setFeatures(features);
        note.setImprovements(improvements);
        note.setBugFixes(bugFixes);
        note.setRawCommits(rawCommits);

        return releaseNoteRepository.save(note);
    }

    private String extractSection(String text, String sectionName) {
        int start = text.indexOf(sectionName);
        if (start == -1) return "None";
        start = text.indexOf("\n", start);
        int end = text.indexOf("\n## ", start + 1);
        return end == -1 ? text.substring(start).trim() : text.substring(start, end).trim();
    }
}
