package com.encore.space.domain.file.controller;

import com.encore.space.common.response.CommonResponse;
import com.encore.space.domain.file.domain.AttachFile;
import com.encore.space.domain.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/file")
@Tag(name = "파일 관련 API")
public class FileController {
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(
            summary = "파일 목록 조회",
            description = "첨부 파일 목록 조회"
    )
    @GetMapping("/list/{postId}")
    public ResponseEntity<CommonResponse> fileList(@PathVariable Long postId){
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "success",
                fileService.getFileList(postId));
    }

    //?
    @Operation(
            summary = "파일 삭제",
            description = "업로드 된 파일 삭제"
    )
    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<CommonResponse> deleteFile(@PathVariable ("fileId") Long id){
        fileService.delete(id);
        return CommonResponse.responseMessage(
                HttpStatus.OK,
                "file deleted successfully");
    }

    @Operation(
            summary = "파일 다운로드",
            description = "첨부 파일 다운로드"
    )
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Long id) throws IOException {
        AttachFile attachFile = fileService.findById(id);
        String encodedFileName = URLEncoder.encode(attachFile.getAttachFileName(), StandardCharsets.UTF_8).replace("+", "%20");
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(fileService.downloadFile(attachFile));
    }

    @Operation(
            summary = "src 이미지 경로",
            description = "이미지 로드"
    )
    @GetMapping("/images/{id}/image")
    public ResponseEntity<Resource> getItem(@PathVariable Long id){
        Resource resource = fileService.getImage(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }
}





