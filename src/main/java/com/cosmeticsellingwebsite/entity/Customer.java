package com.cosmeticsellingwebsite.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity

public class Customer extends User implements Serializable {
    private LocalDate birthDate;
    //    muốn mqh 2 chiều nên vầy có lẽ đc
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Address> addresses;


}
