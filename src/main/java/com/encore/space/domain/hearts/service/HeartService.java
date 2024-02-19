package com.encore.space.domain.hearts.service;

import com.encore.space.domain.comment.domain.Comment;
import com.encore.space.domain.comment.repository.CommentRepository;
import com.encore.space.domain.hearts.HeartRepository;
import com.encore.space.domain.hearts.domain.Heart;
import com.encore.space.domain.hearts.dto.HeartReqDto;
import com.encore.space.domain.member.domain.Member;
import com.encore.space.domain.member.repository.MemberRepository;
import com.encore.space.domain.member.service.MemberService;
import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HeartService {
    private final HeartRepository heartRepository;
    private final MemberService memberService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public HeartService(HeartRepository heartRepository,
                        MemberService memberService,
                        PostRepository postRepository,
                        CommentRepository commentRepository) {
        this.heartRepository = heartRepository;
        this.memberService = memberService;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    //좋아요
    public void giveHeart(HeartReqDto heartReqDto, String email) throws IllegalArgumentException, EntityNotFoundException {
        Member member= memberService.findByEmail(email);
        if(heartReqDto.getPostId()!=null) {
            Post post = postRepository.findById(heartReqDto.getPostId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 게시물입니다."));
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

        if(heartReqDto.getCommentId()!=null) {
            Comment comment = commentRepository.findById(heartReqDto.getCommentId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));
            Optional<Heart> checkHeart = heartRepository.findByMemberAndComment(member, comment);
            if (checkHeart.isPresent()) {
                return;
            }
            Heart heart = Heart.builder()
                    .comment(comment)
                    .member(member)
                    .build();
            heartRepository.save(heart);
        }
    }

    //좋아요 취소
    public void cancelHeart(HeartReqDto heartReqDto, String email) throws IllegalArgumentException,EntityNotFoundException {
        Member member= memberService.findByEmail(email);
        if(heartReqDto.getPostId()!=null) {
            Post post = postRepository.findById(heartReqDto.getPostId()).orElseThrow(()-> new EntityNotFoundException("존재하지 않는 게시물입니다."));
            Optional<Heart> heart= heartRepository.findByMemberAndPost(member,post);
            if (!heart.isPresent()) {
                return;
            }
            heartRepository.delete(heart.orElseThrow());
        }
        if(heartReqDto.getCommentId()!=null) {
            Comment comment = commentRepository.findById(heartReqDto.getCommentId()).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 댓글입니다."));
            Optional<Heart> heart= heartRepository.findByMemberAndComment(member,comment);
            if (!heart.isPresent()) {
                return;
            }
            heartRepository.delete(heart.orElseThrow());
        }
    }

    // 좋아요 개수 구하기

    public long postHearts(Long id){
       return heartRepository.findByPostId(id).size();
    }

    public long commentHearts(Long id){
        return heartRepository.findByCommentId(id).size();
    }

    //좋아요 삭제 (한번에)
    public void deleteHearts(List<Heart> hearts){
        for(Heart h: hearts){
            heartRepository.delete(h);
        }
    }
}
