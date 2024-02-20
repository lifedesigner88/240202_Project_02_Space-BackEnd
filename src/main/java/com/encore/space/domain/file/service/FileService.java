package com.encore.space.domain.file.service;

import com.encore.space.domain.file.domain.AttachFile;
import com.encore.space.domain.file.repository.FileRepository;
import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;
@Slf4j
@Service
@Transactional
public class FileService {
    private final FileRepository fileRepository;
    private final PostRepository postRepository;

    @Autowired
    public FileService(FileRepository fileRepository, PostRepository postRepository) {
        this.fileRepository = fileRepository;
        this.postRepository = postRepository;
    }


    //첨부파일 업로드
    public void uploadAttachFiles(List<MultipartFile> attachFileList, Post post) throws EntityNotFoundException, IllegalArgumentException {

        for (MultipartFile multipartFile : attachFileList) {
            UUID uuid = UUID.randomUUID();
            String attachFileName = uuid + "_" + multipartFile.getOriginalFilename();
            Long attachFileSize = multipartFile.getSize();
            Path path = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/images", attachFileName);        //게시판 ID 값 뒤에 붙여보기

            try {
                byte[] bytes = multipartFile.getBytes();
                Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } catch (IOException e) {
                throw new IllegalArgumentException("image not available");
            }

            AttachFile attachFile = AttachFile.builder()
                    .post(post)
                    .attachFileName(multipartFile.getOriginalFilename())
                    .fileSize(attachFileSize)
                    .attachFilePath(path.toString())
                    .build();
            fileRepository.save(attachFile);
        }
    }

    //파일 목록 조회
    public List<AttachFile> getFileList(Long postId) {
        List<AttachFile> attachFiles = fileRepository.findAllByPostId(postId);
        return attachFiles;
    }

    //파일 다운로드
    public Resource downloadFile(Long fileId, AttachFile attachFile) throws IOException {
        if(attachFile.getDelYN().equals("Y")){
            throw new EntityNotFoundException("이미 삭제된 파일입니다.");
        } else {
            String attachFilePath = attachFile.getAttachFilePath();
            Path path = Paths.get(attachFilePath);
            Resource resource = new org.springframework.core.io.ByteArrayResource(Files.readAllBytes(path));
            return resource;
        }
    }

    public AttachFile findById(Long id) throws EntityNotFoundException {
        AttachFile attachFile = fileRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 파일입니다."));
        if(attachFile.getDelYN().equals("Y")){
            throw new EntityNotFoundException("이미 삭제된 파일입니다.");
        } else {
            return attachFile;
        }
    }

    //업로드 파일 삭제
    public void delete(Long id) throws EntityNotFoundException {
        AttachFile attachFile= fileRepository.findById(id).orElseThrow(()->new EntityNotFoundException("존재하지 않는 파일입니다."));
        if(attachFile.getDelYN().equals("Y")){
            throw new EntityNotFoundException("이미 삭제된 파일입니다.");
        } else {
            attachFile.delete();
        }
    }
}
