package me.jooie.minicafe.service;

import lombok.RequiredArgsConstructor;
import me.jooie.minicafe.domain.*;
import me.jooie.minicafe.repository.OrderRepository;
import me.jooie.minicafe.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public CafeOrder getLatestOrder(String username) {
        CafeUser cafeUser = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("USer Not Found"));

        return orderRepository.findTopByCafeUserOrderByOrderIdDesc(cafeUser)
                .orElseThrow(()-> new RuntimeException("No Order Found"));
    }

    public Page<CafeOrder> getList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<CafeOrder> orders = this.orderRepository.findAll(pageable);
        return orders;
    }

    public void updateOrderStatus(Long orderId, OrderStatus orderStatus){
        CafeOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order Id"));


        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
    }

}
