package com.classic.imteller.api.repository;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Art extends BaseEntity {

    @OneToOne
    @JoinColumn(name="effect_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private Effect effect;

    @ManyToOne
    @JoinColumn(name="designer_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private User designer;

    @ManyToOne
    @JoinColumn(name="owner_id", nullable = false)
    @OnDelete(action= OnDeleteAction.CASCADE)
    private User owner;

    @Column(length=256)
    private String isNFT;

    @Column(nullable = false, length=256)
    private String url;

    @Column(nullable = false, length=20)
    private String title;

    @Column(nullable = false, length=256)
    private String description;

    @Column
    private int recentPrice;

}
