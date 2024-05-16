package org.isd.shop.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    //TYPE IS LONG TEXT
    @Column(name = "note", columnDefinition = "LONGTEXT")
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @Column(name = "status", columnDefinition = "varchar(255) default 'INIT'")
    @Enumerated(EnumType.STRING)
    private Enums.OrderStatus status;


    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetailList;
}