package com.encore.space.domain.file.repository;

import com.encore.space.domain.file.domain.AttachFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<AttachFile, Long> {

    List<AttachFile> findAllByPostId (Long postId);
}
