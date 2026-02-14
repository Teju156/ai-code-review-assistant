package com.codereviewer.backend.controller;

import com.codereviewer.backend.dto.ReleaseNoteDTO;
import com.codereviewer.backend.model.ReleaseNote;
import com.codereviewer.backend.repository.ReleaseNoteRepository;
import com.codereviewer.backend.service.ReleaseNotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/release-notes")
@CrossOrigin(origins = "http://localhost:3000")
public class ReleaseNotesController {

    private final ReleaseNotesService releaseNotesService;
    private final ReleaseNoteRepository releaseNoteRepository;

    @Autowired
    public ReleaseNotesController(ReleaseNotesService releaseNotesService,
                                  ReleaseNoteRepository releaseNoteRepository) {
        this.releaseNotesService = releaseNotesService;
        this.releaseNoteRepository = releaseNoteRepository;
    }

    @PostMapping("/generate")
    public ResponseEntity<ReleaseNote> generate(@RequestBody ReleaseNoteDTO request) {
        ReleaseNote note = releaseNotesService.generateReleaseNotes(
                request.getRepoOwner(), request.getRepoName(), request.getSinceDate()
        );
        return ResponseEntity.ok(note);
    }

    @GetMapping("/history/{owner}/{repo}")
    public ResponseEntity<List<ReleaseNote>> getHistory(
            @PathVariable String owner, @PathVariable String repo) {
        return ResponseEntity.ok(
                releaseNoteRepository.findByRepoOwnerAndRepoNameOrderByGeneratedAtDesc(owner, repo)
        );
    }
}
