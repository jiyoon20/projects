package me.jooie.minicafe.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne
    @JoinColumn(name = "cafe_order_id")
    private CafeOrder cafeOrder;

    private String menuTitle;
    private double price;
    private int quantity;

    public void setCafeOrder(CafeOrder cafeOrder) {
        this.cafeOrder = cafeOrder;
    }
}
