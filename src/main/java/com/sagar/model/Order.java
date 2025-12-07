package com.sagar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
// it is used to designate a java class as a persistent entity. This means that instances of this class can be
// mapped to rows in rdb table
@Data
// it is used to set getters and setters to the class fields and nonstatic variables.
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Users customer;

    @ManyToOne
    @JsonIgnore
    // it is used to instruct the json library to exclude a specific field or method during the serialization of the
    // java object into json or deserialization of json into java
    private Restaurant restaurant;

    private Long totalAmount;

    private String orderStatus;

    private Date createdAt;

    @ManyToOne
    private Address deliveryAddress;

    @OneToMany
    private List<OrderItem> items;

    private int totalItem;

    private Long totalPrice;


}
