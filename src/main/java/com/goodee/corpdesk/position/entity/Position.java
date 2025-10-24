package com.goodee.corpdesk.position.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.goodee.corpdesk.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity @Table(name = "`position`")
@DynamicInsert
@DynamicUpdate
public class Position extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer positionId;

    @Column(name = "parent_position_id", nullable = true)
    private Integer parentPositionId;

    @Column(nullable = false)
    private String positionName;

    public Position(Integer parentPositionId, String positionName) {
        this.parentPositionId = parentPositionId;
        this.positionName = positionName;
    }

}
