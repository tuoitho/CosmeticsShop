package com.cosmeticsellingwebsite.entity;

import com.cosmeticsellingwebsite.enums.Gender;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    protected Long userId;

    @Column(name = "username", unique = true)
    protected String username;

    @Column(name = "password")
    protected String password;

    @Column(name = "email", unique = true)
    protected String email;

    @Column(name = "fullname")
    protected String fullname;

    @Column(name = "phone")
    protected String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    protected Gender gender;

    @Column(name = "image")
    protected String image;

    @Column(name = "active")
    protected Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "roleId", referencedColumnName = "roleId")
    protected Role role;
}