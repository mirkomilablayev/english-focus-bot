package com.ielts.englishfocusbot.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByChatId(String realId);
    Optional<User> findByAdmin(Boolean isAdmin);
    Long countByAdmin(Boolean isAdmin);
}
