package org.isd.shop.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    public static String ADMIN = "ADMIN";
    public static String MANAGER = "MANAGER";
    public static String EMPLOYEE = "EMPLOYEE";
    public static String CUSTOMER = "CUSTOMER";

    public static Long ADMIN_ID = 1L;
    public static Long MANAGER_ID = 2L;
    public static Long EMPLOYEE_ID = 3L;
    public static Long CUSTOMER_ID = 4L;

}
