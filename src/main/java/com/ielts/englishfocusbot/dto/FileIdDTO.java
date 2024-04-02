package com.ielts.englishfocusbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileIdDTO {
    private String fileId;
    private String contentType;
}
