package me.jooie.minicafe.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class CafeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    private CafeUser cafeUser;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderDate;
    private double totalPrice;

    @OneToMany (mappedBy = "cafeOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItemList = new ArrayList<>();

    public CafeOrder(CafeUser cafeUser, OrderStatus orderStatus){
        this.cafeUser = cafeUser;
        this.orderStatus = orderStatus;
        this.orderDate = LocalDateTime.now();
        this.totalPrice = 0.0;
    }

    public void addOrderItems(OrderItem orderItem){
        orderItem.setCafeOrder(this);
        this.orderItemList.add(orderItem);
        this.totalPrice += orderItem.getPrice() * orderItem.getQuantity();
    }

    public double getTotalAmount() {
        if (orderItemList == null) return 0.0;
        return orderItemList.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}