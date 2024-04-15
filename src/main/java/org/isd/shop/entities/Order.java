package org.isd.shop.entities;

import jakarta.persistence.*;
import lombok.*;
import org.isd.shop.enums.Enums;

import java.util.List;

@Entity
@Table(name = "orders")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status", nullable = false, columnDefinition = "varchar(255) default 'INIT'")
    @Enumerated(EnumType.STRING)
    private Enums.OrderStatus status;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetailList;
}