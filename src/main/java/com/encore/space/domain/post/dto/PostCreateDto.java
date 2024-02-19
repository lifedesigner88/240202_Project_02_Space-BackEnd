package com.encore.space.domain.post.dto;

import com.encore.space.domain.post.domain.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateDto {
    private String title;
    private String contents;
    private PostStatus postStatus;
    private List<MultipartFile> attachFileList;
}
