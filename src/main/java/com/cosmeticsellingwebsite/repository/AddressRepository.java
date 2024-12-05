package com.cosmeticsellingwebsite.repository;

import com.cosmeticsellingwebsite.entity.Address;
import com.cosmeticsellingwebsite.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

    List<Address> findAllByCustomer_UserId(Long userId);

    List<Address> findByUser(Customer customer);
}
