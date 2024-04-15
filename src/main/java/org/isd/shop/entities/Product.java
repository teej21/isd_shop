package org.isd.shop.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data//toString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "description", columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "price")
    private double price;

    @Column(name = "thumbnail")
    private String thumbnail;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "material")
    private String material;

    @Column(name = "width")
    private double width;

    @Column(name = "height")
    private double height;

    @Column(name = "publish_year", columnDefinition = "int default null")
    private int publishYear;

}
