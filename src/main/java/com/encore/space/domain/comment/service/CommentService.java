package com.encore.space.domain.comment.service;


import com.encore.space.domain.comment.domain.Comment;
import com.encore.space.domain.comment.dto.CommentChildrenListDto;
import com.encore.space.domain.comment.dto.CommentCreateDto;
import com.encore.space.domain.comment.dto.CommentResDto;
import com.encore.space.domain.comment.dto.CommentUpdateDto;
import com.encore.space.domain.comment.repository.CommentRepository;
import com.encore.space.domain.hearts.HeartRepository;
import com.encore.space.domain.hearts.service.HeartService;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.domain.Role;
import com.encore.space.domain.member.repository.MemberRepository;
import com.encore.space.domain.member.service.MemberService;
import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberService memberService;
    private final HeartRepository heartRepository;
    private final HeartService heartService;
    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, HeartRepository heartRepository, HeartService heartService, MemberService memberService, HeartRepository heartRepository1, HeartService heartService1) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.memberService = memberService;
        this.heartRepository = heartRepository;
        this.heartService = heartService1;
    }

    //댓글 생성
    public void save(CommentCreateDto commentCreateDto, Long postId, String email) throws EntityNotFoundException {
        Member member= memberService.findByEmail(email);
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다."));
        Comment parentComment = null;
        if (commentCreateDto.getParentId() != null) {
            parentComment = commentRepository.findById(commentCreateDto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));
        }
        Comment comment = Comment.builder()
                .post(post)
                .member(member)
                .content(commentCreateDto.getContents())
                .parentComment(parentComment)
                .build();
        commentRepository.save(comment);
    }

    //    댓글 리스트 가져오기
    public List<CommentResDto> findAll(Long postId){
        List<Comment> commentList = commentRepository.findAllParentCommentsByPostId(postId);
        List<CommentResDto> commentResDtoList = new ArrayList<>();
        for(Comment c: commentList) {
            if (c.getDelYN().equals("N")){
                commentResDtoList.add(CommentResDto.toDto(c));
            }
        }
        return commentResDtoList;
    }

    // 대댓글 리스트 가져오기
    public List<CommentChildrenListDto> findChildrenComments(Long postId, Long commentId){
        List<Comment> commentList= commentRepository.findAllByPostIdAndParentCommentId(postId,commentId);
        List<CommentChildrenListDto> childrenList= new ArrayList<>();
        for(Comment c: commentList) {
            if (c.getDelYN().equals("N")) {
                childrenList.add(CommentChildrenListDto.toDto(c));
            }
        }
        return childrenList;
    }


    //댓글 수정
    public void update(Long id, CommentUpdateDto commentUpdateDto, String email) throws EntityNotFoundException,AccessDeniedException {
        Member member= memberService.findByEmail(email);
        Comment comment = commentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("이미 삭제됐거나 등록되지 않은 댓글입니다."));
        if(!Objects.equals(member.getId(), comment.getMember().getId())){
            throw new AccessDeniedException ("권한이 없습니다.");
        }
        comment.updateComment(commentUpdateDto.getContents());
    }

    //댓글 삭제
    @Transactional
    public void delete(Long commentId, String email) throws EntityNotFoundException,AccessDeniedException {
        Member member= memberService.findByEmail(email);
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new EntityNotFoundException("작성된 댓글이 없습니다."));
        if(Objects.equals(member.getId(), comment.getMember().getId()) || member.getRole().equals(Role.MANAGER) || member.getRole().equals(Role.TEACHER)) {
            if (comment.getChildrenComment() != null) {
                for (Comment c : comment.getChildrenComment()) {
                    if (c.getDelYN().equals("N")) {
                        heartService.deleteHearts(heartRepository.findByCommentId(c.getId()));
                        c.delete();
                    }
                }
            }
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
        heartService.deleteHearts(heartRepository.findByCommentId(commentId));
        comment.delete();
    }
}
