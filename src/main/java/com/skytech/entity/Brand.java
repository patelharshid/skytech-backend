package com.skytech.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "logo_url", columnDefinition = "TEXT")
    private String logoUrl;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_date_time", updatable = false)
    private LocalDateTime createdDateTime;

    @PrePersist
    protected void onCreate() {
        if (createdDateTime == null) {
            createdDateTime = LocalDateTime.now();
        }
        if (isActive == null) {
            isActive = true;
        }
    }
}
