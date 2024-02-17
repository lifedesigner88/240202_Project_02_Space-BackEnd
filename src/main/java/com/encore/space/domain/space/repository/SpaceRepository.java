package com.encore.space.domain.space.repository;

import com.encore.space.domain.space.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {

}
