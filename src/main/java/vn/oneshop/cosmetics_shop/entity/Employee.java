package vn.oneshop.cosmetics_shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Employee")
public class Employee extends User{
    private String fullName;
    private String phone;
    private String gender;
    private String address;
    private Date startDate;
}
