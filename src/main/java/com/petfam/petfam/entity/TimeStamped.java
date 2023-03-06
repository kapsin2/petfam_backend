package com.petfam.petfam.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass  // 상속시, 자동으로 컬럼으로 인식하게 함
@EntityListeners(AuditingEntityListener.class)  // 시간을 자동으로 반영하도록 설정해줌
public class TimeStamped {

  @CreatedDate
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime modifiedAt;
}
