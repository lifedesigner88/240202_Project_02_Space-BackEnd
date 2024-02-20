package com.encore.space.domain.post.service;

import com.encore.space.common.domain.ChangeType;
import com.encore.space.domain.comment.domain.Comment;
import com.encore.space.domain.comment.repository.CommentRepository;
import com.encore.space.domain.comment.service.CommentService;
import com.encore.space.domain.file.service.FileService;
import com.encore.space.domain.hearts.repository.HeartRepository;
import com.encore.space.domain.hearts.service.HeartService;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.domain.Role;
import com.encore.space.domain.member.service.MemberService;
import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.post.dto.PostCreateDto;
import com.encore.space.domain.post.dto.PostDetailResDto;
import com.encore.space.domain.post.dto.PostListDto;
import com.encore.space.domain.post.dto.PostUpdateDto;
import com.encore.space.domain.post.repository.PostRepository;
import com.encore.space.domain.space.domain.Space;
import com.encore.space.domain.space.service.SpaceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final FileService fileService;
    private final HeartService heartService;
    private final MemberService memberService;
    private final SpaceService spaceService;
    private final CommentRepository commentRepository;
    private final ChangeType changeType;

    @Autowired
    public PostService(PostRepository postRepository,
                       FileService fileService,
                       HeartService heartService,
                       MemberService memberService,
                       SpaceService spaceService,
                       CommentRepository commentRepository,
                       ChangeType changeType) {
        this.postRepository = postRepository;
        this.fileService = fileService;
        this.heartService = heartService;
        this.memberService = memberService;
        this.spaceService = spaceService;
        this.commentRepository = commentRepository;
        this.changeType = changeType;
    }

    //게시글 가져오기
    public Post findByPostId(Long postId) throws EntityNotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(EntityNotFoundException::new);
        if(post.getDelYN().equals("Y")){
            throw new EntityNotFoundException("삭제된 게시글입니다.");
        }
        return post;
    }

    public List<Post> getPosts(){
        return postRepository.findAll();
    }


    //게시글 저장 with 이미지파일(첨부파일)
    public void save(PostCreateDto postCreateDto, String email) throws EntityNotFoundException {
        Member member = memberService.findByEmail(email);
        Space space = spaceService.findSpaceBySapceId(postCreateDto.getSpaceId());
        Post post = postRepository.save(changeType.postCreateDtoTOPost(postCreateDto, member, space));
        if (!postCreateDto.getThumbnail().isEmpty()){
            fileService.setThumbnail(postCreateDto.getThumbnail(),post);
        }
        if (!postCreateDto.getAttachFileList().isEmpty()) {
            fileService.uploadAttachFiles(postCreateDto.getAttachFileList(), post);
        }
    }

    //게시글 상세 조회
    public PostDetailResDto getPost(Long postId) throws EntityNotFoundException {
        Post post = this.findByPostId(postId);
        Long postHearts = heartService.postHearts(postId);
        int commentCounts= commentRepository.findAllByPostId(postId).size();
        return changeType.postTOPostDetailResDto(post, postHearts, commentCounts);
    }

    //게시글 목록 조회
    public List<PostListDto> findAll() {
        List<PostListDto> postListDtoList = new ArrayList<>();
        List<Post> posts= this.getPosts();
        for (Post p : posts) {
            if (p.getDelYN().equals("N")) {
                postListDtoList.add(changeType.postTOPostListDto(p));
            }
        }
        return postListDtoList;
    }

    //게시글 수정
    public void updatePost(Long id, PostUpdateDto postUpdateDto, String email) throws AccessDeniedException {
        Member member = memberService.findByEmail(email);
        Post post = this.findByPostId(id);
        if (Objects.equals(member.getId(), post.getMember().getId())) {
            post.updatePost(postUpdateDto.getTitle(), postUpdateDto.getContents(), postUpdateDto.getPostStatus());
            if (!postUpdateDto.getThumbnail().isEmpty()){
                fileService.setThumbnail(postUpdateDto.getThumbnail(),post);
            }
            if (!postUpdateDto.getAttachFileList().isEmpty()) {
                fileService.uploadAttachFiles(postUpdateDto.getAttachFileList(), post);
            }
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }

    //게시글 삭제
    public void delete(Long postId, String email) throws EntityNotFoundException, AccessDeniedException {
        Member member = memberService.findByEmail(email);
        Post post = this.findByPostId(postId);
        if (Objects.equals(member.getId(), post.getMember().getId()) || member.getRole().equals(Role.MANAGER) || member.getRole().equals(Role.TEACHER)) {
            List<Comment> postComment = commentRepository.findAllByPostId(postId);
            for (Comment c : postComment) {
                c.delete();
            }
            heartService.deleteHearts(heartService.getPostHearts(postId));
            post.deletePost();
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }
}