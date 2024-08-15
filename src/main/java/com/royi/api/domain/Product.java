package com.royi.api.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {

    @Id
    private Long productId;

    @NotEmpty
    private String productName;

    @Positive
    private Double price;
}
