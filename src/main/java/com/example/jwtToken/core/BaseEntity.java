package com.example.jwtToken.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@RequiredArgsConstructor
@Setter
abstract class BaseEntity {

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy; // ذخیره username سازنده رکورد

    @CreatedDate
    @Column(name = "insert_date",updatable = false)
    private LocalDateTime insertDate;

    @LastModifiedBy
    @Column(name = "last_update_by",updatable = true)
    private String lastUpdatedBy;

    @LastModifiedDate
    @Column(name = "last_update_date",updatable = true)
    private LocalDateTime lastUpdateDate;

    // getters
}