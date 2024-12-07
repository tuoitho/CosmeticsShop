package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Role;
import com.cosmeticsellingwebsite.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleEnum name);

    @Query("SELECT r FROM Role r WHERE r.roleName IN :roleNames")
    List<Role> findByRoleNameIn(@Param("roleNames") List<String> roleNames);
}
