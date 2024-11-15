package vn.oneshop.cosmetics_shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Customer")
public class Customer extends User{
    private String fullName;
    private String phone;
    private String gender;
    private Date birthday;

    @OneToMany(mappedBy = "customer")
   private List<Address> addresses;
}
