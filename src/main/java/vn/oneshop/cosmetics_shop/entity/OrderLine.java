package vn.oneshop.cosmetics_shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "OrderLine")
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderLineId;
    private Long quantity;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Product product;

    // Additional field for product snapshot
    @ElementCollection
    private Map<String, Object> productSnapshot;
}
