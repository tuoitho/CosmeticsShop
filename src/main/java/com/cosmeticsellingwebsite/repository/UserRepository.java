package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.User;
import com.cosmeticsellingwebsite.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findByFullnameContainingOrUsernameContainingOrEmailContaining(String fullname, String username, String email);

    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword% or u.fullname LIKE %:keyword%")
    Page<User> search(String keyword, Pageable pageable);


    Page<User> findAll(Pageable pageable);

    Page<User> findByRole_RoleNameInAndUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            List<RoleEnum> roleNames, String username, String email, Pageable pageable);

    Page<User> findByRole_RoleNameIn(List<RoleEnum> roleNames, Pageable pageable);

}
