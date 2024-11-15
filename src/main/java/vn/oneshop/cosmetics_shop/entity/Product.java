package vn.oneshop.cosmetics_shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productCode;
    private String productName;
    private float cost;
    private String description;
    private String brand;
    private Date expirationDate;
    private Date manufactureDate;
    private String ingredient;
    private String howToUse;
    private String volume;
    private String origin;
    private String image;
    private boolean active;

    @ManyToOne
    private Category category;

    @OneToOne(mappedBy = "product")
    private ProductStock productStock;
}
