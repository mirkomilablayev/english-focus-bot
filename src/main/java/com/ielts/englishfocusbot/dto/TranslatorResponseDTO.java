package com.ielts.englishfocusbot.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranslatorResponseDTO {
    private String translatedText;
    private String from;
    private String to;
}
