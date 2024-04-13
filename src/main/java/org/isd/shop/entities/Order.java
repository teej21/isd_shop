package org.isd.shop.entities;

import jakarta.persistence.*;
import org.isd.shop.enums.Enums;

import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status", nullable = false, columnDefinition = "varchar(255) default 'PENDING'")
    @Enumerated(EnumType.STRING)
    private Enums.OrderStatus status;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetailList;
}