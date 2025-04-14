package me.jooie.minicafe.domain;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;

    @Column(name="coffeeImageUrl")
    private String coffeeImageUrl;

    @Column(name="title", nullable=false)
    private String title;

    @Column(name="price")
    private Double price;

    @Column(name="content")
    private String content;

    @Column(name="calories")
    private Double calories;

}
