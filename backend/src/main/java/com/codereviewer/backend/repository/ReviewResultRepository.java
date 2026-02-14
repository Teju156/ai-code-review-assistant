package com.codereviewer.backend.repository;

import com.codereviewer.backend.model.ReviewResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewResultRepository extends JpaRepository<ReviewResult, Long> {
    List<ReviewResult> findByRepoOwnerAndRepoNameOrderByCreatedAtDesc(String owner, String repo);
}
