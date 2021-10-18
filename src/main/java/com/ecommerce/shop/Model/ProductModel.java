package com.ecommerce.shop.Model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "PRODUCT")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ProductModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal defaultPrice;
    private int stock;
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategoriesModel categories;
}
