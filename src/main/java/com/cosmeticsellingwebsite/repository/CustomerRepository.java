package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByFullnameOrEmail(String name, String email);

    @Query("SELECT c FROM Customer c")
    List<Customer> findAllCustomers();

    List<Customer> findByFullnameContainingOrEmailContaining(String fullname, String email);

}
