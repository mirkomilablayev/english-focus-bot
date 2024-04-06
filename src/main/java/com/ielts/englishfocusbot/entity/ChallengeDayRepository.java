package com.ielts.englishfocusbot.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeDayRepository extends JpaRepository<ChallengeDay, Long> {
    Optional<ChallengeDay> findByDone(boolean done);

    Long countByDone(boolean done);

}
