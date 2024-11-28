package com.cosmeticsellingwebsite.entity;

import com.cosmeticsellingwebsite.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    @Column(columnDefinition = "text")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;
}
