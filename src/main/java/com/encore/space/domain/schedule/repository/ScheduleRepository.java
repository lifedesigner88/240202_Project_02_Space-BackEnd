package com.encore.space.domain.schedule.repository;

import com.encore.space.domain.schedule.domain.Schedule;
import com.encore.space.domain.space.domain.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findBySpace(Space space);
}
