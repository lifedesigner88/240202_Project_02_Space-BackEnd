package com.encore.space.domain.file.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttachFileReqDto {
    private String attachFileName;
    private String attachFilePath;
    private Long fileSize;
    private Long postId;
}
