package com.cosmeticsellingwebsite.entity;

import com.cosmeticsellingwebsite.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "text")
    private RoleEnum roleName;
}