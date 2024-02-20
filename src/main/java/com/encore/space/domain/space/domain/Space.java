package com.encore.space.domain.space.domain;

import com.encore.space.common.domain.BaseEntity;
import com.encore.space.domain.post.domain.Post;
import com.encore.space.domain.schedule.domain.Schedule;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Space extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String spaceName;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private SpaceType spaceType;

    @Setter
    private String description;

    @Setter
    private String spaceThumbNailPath;

    @Setter
    private String delYn = "N";

    @Builder
    public Space(String spaceName, SpaceType spaceType,
                 String description, String spaceThumbNailPath) {
        this.spaceName = spaceName;
        this.spaceType = spaceType;
        this.description = description;
        this.spaceThumbNailPath = spaceThumbNailPath;
    }

    public Space() {}

    @OneToMany(mappedBy = "space", cascade = CascadeType.PERSIST)
    private final List<SpaceMember> spaceMembers = new ArrayList<>();
    public void setSpaceMembers(List<SpaceMember> spaceMembers) {
        this.spaceMembers.clear();
        if (spaceMembers != null) {
            this.spaceMembers.addAll(spaceMembers);
        }
    }

    @OneToMany(mappedBy = "space", cascade = CascadeType.PERSIST)
    private final List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "space", cascade = CascadeType.PERSIST)
    private final List<Post> posts = new ArrayList<>();


}

