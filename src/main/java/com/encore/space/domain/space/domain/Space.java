package com.encore.space.domain.space.domain;

import com.encore.space.common.domain.BaseEntity;
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

    private String description;
    private String spaceThumbNailPath;

    @OneToMany(mappedBy = "space", cascade = CascadeType.PERSIST)
    private final List<SpaceMember> spaceMembers = new ArrayList<>();

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

    public void setSpaceMembers(List<SpaceMember> spaceMembers) {
        this.spaceMembers.clear();
        if (spaceMembers != null) {
            this.spaceMembers.addAll(spaceMembers);
        }
    }
}

