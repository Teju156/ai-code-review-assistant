package com.codereviewer.backend.repository;

import com.codereviewer.backend.model.ReleaseNote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReleaseNoteRepository extends JpaRepository<ReleaseNote, Long> {
    List<ReleaseNote> findByRepoOwnerAndRepoNameOrderByGeneratedAtDesc(String owner, String repo);
}
