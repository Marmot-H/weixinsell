package com.yongqi.sell.dataobject;

import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;

//@Table(name = "product_category")
@Entity
@Proxy(lazy = false)
@Data
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;
    private String categoryName;
    private Integer categoryType;
}
