package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Tìm kiếm khách hàng theo tên hoặc email
    @Query("SELECT c FROM Customer c WHERE LOWER(c.fullname) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Customer> findByFullnameOrEmail(@Param("keyword") String keyword);

    @Query("SELECT c FROM Customer c")
    List<Customer> findAllCustomers();

    List<Customer> findByFullnameContainingOrEmailContaining(String fullname, String email);

    Page<Customer> findByFullnameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String fullName, String email, Pageable pageable);

}
