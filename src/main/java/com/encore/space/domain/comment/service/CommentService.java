package com.encore.space.domain.comment.service;


import com.encore.space.common.domain.ChangeType;
import com.encore.space.domain.comment.domain.Comment;
import com.encore.space.domain.comment.dto.CommentChildrenListDto;
import com.encore.space.domain.comment.dto.CommentCreateDto;
import com.encore.space.domain.comment.dto.CommentResDto;
import com.encore.space.domain.comment.dto.CommentUpdateDto;
import com.encore.space.domain.comment.repository.CommentRepository;
import com.encore.space.domain.hearts.repository.HeartRepository;
import com.encore.space.domain.hearts.service.HeartService;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.domain.Role;
import com.encore.space.domain.member.service.MemberService;
import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.post.repository.PostRepository;
import com.encore.space.domain.post.service.PostService;
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
    private final PostService postService;
    private final MemberService memberService;
    private final HeartService heartService;
    private final ChangeType changeType;
    @Autowired
    public CommentService(CommentRepository commentRepository,
                          PostService postService,
                          HeartService heartService,
                          MemberService memberService, ChangeType changeType) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.memberService = memberService;
        this.heartService = heartService;
        this.changeType = changeType;
    }

    public Comment findByCommentId(Long commentId) throws EntityNotFoundException {
        return commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);
    }

    //댓글 목록(post Id와 부모댓글까지)
    public List<Comment> findAllByPostId(Long postId){
        return commentRepository.findAllParentCommentsByPostId(postId);
    }

    public List<Comment> findAllByPostIdAndParentCommentId(Long postId, Long parentCommentId){
        return commentRepository.findAllByPostIdAndParentCommentId(postId,parentCommentId);
    }


    //댓글 생성
    public void save(CommentCreateDto commentCreateDto, Long postId, String email) throws EntityNotFoundException {
        Member member= memberService.findByEmail(email);
        Post post= postService.findByPostId(postId);
        Comment parentComment = null;
        if (commentCreateDto.getParentId() != null) {
            parentComment = this.findByCommentId(commentCreateDto.getParentId());
        }
        Comment newComment=changeType.commentCreateDtoTOComment(commentCreateDto,member, post,parentComment);
        commentRepository.save(newComment);
    }


    //    댓글 리스트 가져오기
    public List<CommentResDto> findAll(Long postId){
        List<CommentResDto> commentResDtoList = new ArrayList<>();
        List<Comment> commentList = this.findAllByPostId(postId);
        for(Comment c: commentList) {
            commentResDtoList.add(changeType.commentTOCommentResDto(c));
        }
        return commentResDtoList;
    }


    // 대댓글 리스트 가져오기
    public List<CommentChildrenListDto> findChildrenComments(Long postId, Long commentId) throws EntityNotFoundException {
        List<CommentChildrenListDto> childrenList= new ArrayList<>();
        if(postService.findByPostId(postId).getDelYN().equals("N") && commentRepository.findById(commentId).isPresent()){
            List<Comment> commentList= this.findAllByPostIdAndParentCommentId(postId,commentId);
            for(Comment c: commentList) {
                childrenList.add(changeType.commentTOChildrenListDto(c));
            }
            return childrenList;
        } else {
            throw new EntityNotFoundException();
        }
    }


    //댓글 수정
    public void update(Long commentId,String email, CommentUpdateDto commentUpdateDto) throws AccessDeniedException {
        Member member= memberService.findByEmail(email);
        Comment comment = this.findByCommentId(commentId);
        if(!Objects.equals(member.getId(), comment.getMember().getId())){
            throw new AccessDeniedException ("권한이 없습니다.");
        }
        comment.updateComment(commentUpdateDto.getContents());
    }


    //댓글 삭제 (대댓글은 삭제 X)
    public void delete(Long commentId, String email) throws IllegalArgumentException,AccessDeniedException {
        Member member = memberService.findByEmail(email);
        Comment comment = this.findByCommentId(commentId);
        if (Objects.equals(member.getId(), comment.getMember().getId())
                || member.getRole().equals(Role.MANAGER) || member.getRole().equals(Role.TEACHER)) {
            if (comment.getDelYN().equals("Y")) {
                throw new IllegalArgumentException("이미 삭제된 댓글입니다.");
            } else {
                heartService.deleteHearts(heartService.getCommentHearts(commentId));
                comment.delete();
            }
        } else {
            throw new AccessDeniedException("권한이 없습니다.");
        }
    }
}
