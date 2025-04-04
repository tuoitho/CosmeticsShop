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
    protected Long userId;

    @Column(unique = true)
    protected String username;

    protected String password;

    @Column( unique = true)
    protected String email;

    protected String fullname;

    protected String phone;

    @Enumerated(EnumType.STRING)
    protected Gender gender;

    protected String image;

    protected Boolean active = true;

    @ManyToOne
    @ToString.Exclude     @EqualsAndHashCode.Exclude

    @JoinColumn(name = "roleId")
    protected Role role;
}