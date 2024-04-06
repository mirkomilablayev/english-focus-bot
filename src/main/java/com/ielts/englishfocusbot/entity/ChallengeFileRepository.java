package com.ielts.englishfocusbot.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChallengeFileRepository extends JpaRepository<ChallengeFile, Long> {
    List<ChallengeFile> findAllByChallengeDayId(Long challengeDayId);
    Long countByChallengeDayId(Long challengeDayId);
}
