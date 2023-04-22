package com.sparta.testlevel1.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)  //해당 클래스에 Auditing 기능을 포함
public class Timestamped {

    @CreationTimestamp
    private LocalDateTime createdAt;

    @LastModifiedDate LocalDateTime modifiedAt;
}
