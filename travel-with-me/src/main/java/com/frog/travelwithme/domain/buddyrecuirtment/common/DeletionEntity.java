package com.frog.travelwithme.domain.buddyrecuirtment.common;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

/**
 * 작성자: 이재혁
 * 버전 정보: 1.0.0
 * 작성일자: 2023/04/12
 **/

@Setter
@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class DeletionEntity {

    @ColumnDefault(value = "false")
    private Boolean isDeleted;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

}
