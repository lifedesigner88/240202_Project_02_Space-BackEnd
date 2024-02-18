package com.encore.space.domain.schedule.domain;

import com.encore.space.common.domain.BaseEntity;
import com.encore.space.domain.space.domain.Space;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String scheduleTitle;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @Setter
    private String description;

    @Setter
    @Enumerated(value = EnumType.STRING)
    private KanBanStatus kanBanStatus;

    @Setter
    private String isKanBan = "N";

    @Setter
    private String delYn = "N";

    @Setter
    @ManyToOne
    @JoinColumn(nullable = false)
    private Space space;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.PERSIST)
    private final List<ScheduleMember> scheduleMembers = new ArrayList<>();
    public void addScheduleMember(ScheduleMember scheduleMember) {
        scheduleMembers.add(scheduleMember);
    }

    @Builder
    public Schedule(String scheduleTitle, LocalDateTime startDateTime,
                    LocalDateTime endDateTime, String description, KanBanStatus status, String isKanBan) {

        this.scheduleTitle = scheduleTitle;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.description = description;
        this.kanBanStatus = status;
        this.isKanBan = isKanBan;
    }

    public Schedule(){}
}
