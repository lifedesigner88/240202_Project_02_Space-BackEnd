package com.encore.space.domain.file.service;

import com.encore.space.common.domain.ChangeType;
import com.encore.space.domain.file.domain.AttachFile;
import com.encore.space.domain.file.repository.FileRepository;
import com.encore.space.domain.post.domain.Post;
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
import java.util.Objects;
import java.util.UUID;
@Slf4j
@Service
@Transactional
public class FileService {
    private final FileRepository fileRepository;
    private final ChangeType changeType;

    @Autowired
    public FileService(FileRepository fileRepository, ChangeType changeType) {
        this.fileRepository = fileRepository;
        this.changeType = changeType;
    }

    //썸네일 업로드
    public void setThumbnail(MultipartFile thumbnail,Post post){
        UUID uuid = UUID.randomUUID();
        String thumbnailFileName = uuid + "_thumbnail_" + thumbnail.getOriginalFilename();
        Path thumbnailPath = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/images", thumbnailFileName);
        try {
            byte[] bytes = thumbnail.getBytes();
            Files.write(thumbnailPath, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            fileRepository.save(changeType.toAttachFile(thumbnail,post,thumbnailPath));
            post.setThumbnail(thumbnailPath.toString());
        } catch (IOException e) {
            throw new IllegalArgumentException("image not available");
        }
    }

    public void updateThumbnail(MultipartFile thumbnail,Post post){
        if(post.getThumbnail()!=null){
            AttachFile oldThumb= fileRepository.findByAttachFilePath(post.getThumbnail());
            oldThumb.delete();
            post.deleteThumbnail();
            this.setThumbnail(thumbnail,post);
        } else {
            this.setThumbnail(thumbnail, post);
        }
    }

    //첨부파일 업로드
    public void uploadAttachFiles(List<MultipartFile> attachFileList, Post post) throws EntityNotFoundException, IllegalArgumentException {
        for (MultipartFile multipartFile : attachFileList) {
            try {
                if(!Objects.requireNonNull(multipartFile.getOriginalFilename()).isEmpty()){
                    UUID uuid = UUID.randomUUID();
                    String attachFileName = uuid + "_" + multipartFile.getOriginalFilename();
                    Path path = Paths.get(System.getProperty("user.dir") + "/src/main/resources/static/images", attachFileName);        //게시판 ID 값 뒤에 붙여보기

                    byte[] bytes = multipartFile.getBytes();
                    Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                    fileRepository.save(changeType.toAttachFile(multipartFile,post,path));
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("file not available");
            }
        }
    }

    //파일 목록 조회
    public List<AttachFile> getFileList(Long postId) {
        return fileRepository.findAllByPostId(postId);
    }

    //파일 다운로드
    public Resource downloadFile(AttachFile attachFile) throws IOException {
        if(attachFile.getDelYN().equals("Y")){
            throw new EntityNotFoundException("이미 삭제된 파일입니다.");
        } else {
            String attachFilePath = attachFile.getAttachFilePath();
            Path path = Paths.get(attachFilePath);
            return new org.springframework.core.io.ByteArrayResource(Files.readAllBytes(path));
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
