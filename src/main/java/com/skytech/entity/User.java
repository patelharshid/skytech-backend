package com.skytech.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", length = 100)
    private String firstname;

    @Column(name = "lastname", length = 100)
    private String lastname;

    @Column(name = "mobileno", length = 20)
    private String mobileno;

    @Column(name = "email", unique = true, length = 150)
    private String email;

    @Column(name = "hashkey")
    private String hashkey;

    @Column(name = "password")
    private String password;

    @Column(name = "isactive", nullable = false)
    @Builder.Default
    private Boolean isactive = true;

    @Column(name = "createby")
    private Integer createby;

    @Column(name = "createddatetime", updatable = false)
    private LocalDateTime createddatetime;

    @Column(name = "updatedatetime")
    private LocalDateTime updatedatetime;
}
