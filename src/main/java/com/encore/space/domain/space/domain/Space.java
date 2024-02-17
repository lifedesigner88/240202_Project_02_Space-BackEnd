package com.encore.space.domain.space.domain;

import com.encore.space.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


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
}

