package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Role;
import com.cosmeticsellingwebsite.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleEnum name);
}
