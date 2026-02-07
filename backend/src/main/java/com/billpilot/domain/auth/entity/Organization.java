package com.billpilot.domain.auth.entity;

import com.billpilot.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Organization extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(name = "org_code", nullable = false, unique = true, length = 10)
    private String orgCode;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}
