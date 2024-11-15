package vn.oneshop.cosmetics_shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table (name = "Order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Date orderDate;
    private String status;
    private Date deliveryDate;
    private Double total;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private ShippingAddress address;

    @OneToMany(mappedBy = "order")
    private List<OrderLine> orderLines;
}
