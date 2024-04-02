package com.ielts.englishfocusbot.entity;

import com.ielts.englishfocusbot.util.Lang;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chatId;
    private String step;
    private Boolean isAdmin = false;
    @Enumerated(EnumType.STRING)
    private Lang fromLang;
    @Enumerated(EnumType.STRING)
    private Lang toLang;


}
