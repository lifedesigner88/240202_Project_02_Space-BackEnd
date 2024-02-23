package com.encore.space.domain.file.service;

import com.encore.space.common.domain.ChangeType;
import com.encore.space.domain.file.domain.AttachFile;
import com.encore.space.domain.file.repository.FileRepository;
import com.encore.space.domain.post.domain.Post;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
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
        Path thumbnailPath = Paths.get(System.getProperty("user.dir") + "/src/main/java/com/encore/space/images", thumbnailFileName);
        try {
            byte[] bytes = thumbnail.getBytes();
            Files.write(thumbnailPath, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            String isThumbnail="Y";
            AttachFile attachFile = fileRepository.save(changeType.toAttachFile(thumbnail,post, thumbnailPath.toString(), isThumbnail));
            post.setThumbnail(attachFile.getId().toString());
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
    public void uploadAttachFiles(List<MultipartFile> attachFileList, Post post, List<String> imgUrlList) throws EntityNotFoundException, IllegalArgumentException {
        for (int i = 0; i< attachFileList.size(); i++) {
            try {
                if(!Objects.requireNonNull(attachFileList.get(i).getOriginalFilename()).isEmpty()){
                    UUID uuid = UUID.randomUUID();
                    String attachFileName = uuid + "_" + attachFileList.get(i).getOriginalFilename();
                    Path path = Paths.get(System.getProperty("user.dir") + "/src/main/java/com/encore/space/images", attachFileName);        //게시판 ID 값 뒤에 붙여보기
                    String isThumbnail="N";
                    byte[] bytes = attachFileList.get(i).getBytes();
                    Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                    AttachFile attachFile = fileRepository.save(changeType.toAttachFile(attachFileList.get(i),post,System.getProperty("user.dir") + "/src/main/java/com/encore/space/images/"+ attachFileName ,isThumbnail));
                    post.setContents(post.getContents().replace("src=", ":src=").replace(imgUrlList.get(0),"http://localhost:8080//api/file/images/"+attachFile.getId()+"/image"));

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

    //업로드 파일 삭제
    public void delete(Long id) throws EntityNotFoundException {
        AttachFile attachFile= fileRepository.findById(id).orElseThrow(()->new EntityNotFoundException("존재하지 않는 파일입니다."));
        if(attachFile.getDelYN().equals("Y")){
            throw new EntityNotFoundException("이미 삭제된 파일입니다.");
        } else {
            attachFile.delete();
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

    // 파일 경로로 파일 가져오는 함수
    public AttachFile findByFilePath(String path){
        return fileRepository.findByAttachFilePath(path);
    }

    public Resource getImage(Long id) {
        AttachFile item = this.findById(id);
        String imagePath = item.getAttachFilePath();
        Resource resource;
        Path path = Paths.get(imagePath);
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("url error");
        }
        return resource;
    }
}
