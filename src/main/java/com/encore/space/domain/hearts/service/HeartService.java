package com.encore.space.domain.hearts.service;

import com.encore.space.domain.comment.domain.Comment;
import com.encore.space.domain.comment.repository.CommentRepository;
import com.encore.space.domain.comment.service.CommentService;
import com.encore.space.domain.hearts.repository.HeartRepository;
import com.encore.space.domain.hearts.domain.Heart;
import com.encore.space.domain.hearts.dto.HeartReqDto;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.service.MemberService;
import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.post.repository.PostRepository;
import com.encore.space.domain.post.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HeartService {
    private final HeartRepository heartRepository;
    private final CommentRepository commentRepository;
    private final MemberService memberService;

    private final PostRepository postRepository;

    @Autowired
    public HeartService(HeartRepository heartRepository,
                        MemberService memberService,
                        CommentRepository commentRepository,
                        PostRepository postRepository) {
        this.heartRepository = heartRepository;
        this.memberService = memberService;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    //좋아요
    public void giveHeart(HeartReqDto heartReqDto, String email) throws EntityNotFoundException {
        Member member= memberService.findByEmail(email);

    //게시글에 좋아요 한 경우
        if(heartReqDto.getPostId()!=null) {
            Post post= postRepository.findById(heartReqDto.getPostId()).orElseThrow(EntityNotFoundException::new);
            if(post.getDelYN().equals("N")){
                Optional<Heart> checkHeart = heartRepository.findByMemberAndPost(member, post);
                if (checkHeart.isPresent()) {
                    return;
                }
                Heart heart = Heart.builder()
                        .post(post)
                        .member(member)
                        .build();
                heartRepository.save(heart);
            } else{
                throw new EntityNotFoundException("게시글이 존재하지 않습니다.");
            }
        }

    //댓글에 좋아요 한 경우
        if(heartReqDto.getCommentId()!=null) {
            Comment comment = commentRepository.findById(heartReqDto.getCommentId()).orElseThrow(EntityNotFoundException::new);
            if(comment.getDelYN().equals("N")){
                Optional<Heart> checkHeart = heartRepository.findByMemberAndComment(member, comment);
                if (checkHeart.isPresent()) {
                    return;
                }
                Heart heart = Heart.builder()
                        .comment(comment)
                        .member(member)
                        .build();
                heartRepository.save(heart);
            } else {
                throw new EntityNotFoundException("댓글이 존재하지 않습니다.");
            }
        }
    }

    //좋아요 취소
    public void cancelHeart(HeartReqDto heartReqDto, String email) throws IllegalArgumentException,EntityNotFoundException {
        Member member= memberService.findByEmail(email);
        if(heartReqDto.getPostId()!=null) {
            Post post= postRepository.findById(heartReqDto.getPostId()).orElseThrow(EntityNotFoundException::new);
            Optional<Heart> heart= heartRepository.findByMemberAndPost(member,post);
            if (!heart.isPresent()) {
                return;
            }
            heartRepository.delete(heart.orElseThrow());
        }
        if(heartReqDto.getCommentId()!=null) {
            Comment comment = commentRepository.findById(heartReqDto.getCommentId()).orElseThrow(EntityNotFoundException::new);
            Optional<Heart> heart= heartRepository.findByMemberAndComment(member,comment);
            if (!heart.isPresent()) {
                return;
            }
            heartRepository.delete(heart.orElseThrow());
        }
    }

    //좋아요 삭제 (한번에)
    public void deleteHearts(List<Heart> hearts){
        for(Heart h: hearts){
            heartRepository.delete(h);
        }
    }
    public List<Heart> getPostHearts(Long postId){
        return heartRepository.findByPostId(postId);
    }

    public List<Heart> getCommentHearts(Long commentId){
        return heartRepository.findByCommentId(commentId);
    }



    // 좋아요 개수 구하기
    public long postHearts(Long id){
        return heartRepository.findByPostId(id).size();
    }

    public long commentHearts(Long id){
        return heartRepository.findByCommentId(id).size();
    }
}
