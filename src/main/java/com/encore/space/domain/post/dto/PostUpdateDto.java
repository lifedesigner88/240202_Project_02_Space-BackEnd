package com.encore.space.domain.post.dto;

import com.encore.space.domain.post.domain.PostStatus;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostUpdateDto {
    private String title;
    private String contents;
    private PostStatus postStatus;
    private List<MultipartFile> attachFileList;
}
