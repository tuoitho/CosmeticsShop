package com.cosmeticsellingwebsite.payload.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddProductRequest {
    @NotNull(message = "Product code cannot be null")
    private String productCode;
    @NotNull
    private String productName;
    private Double cost;
    private String description;
    private String brand;
    @NotNull
    private LocalDate manufactureDate;
    @NotNull
    private LocalDate expirationDate;
    private String ingredient;
    private String how_to_use;
    private String volume;
    private String origin;
//    skip image field when serializing and deserializing JSON
    @JsonIgnore
    private String image;
    private Long categoryId;
    private Long stock;
}
