package me.jooie.minicafe.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import me.jooie.minicafe.domain.*;
import me.jooie.minicafe.repository.CartRepository;
import me.jooie.minicafe.repository.MenuRepository;
import me.jooie.minicafe.repository.OrderRepository;
import me.jooie.minicafe.repository.UserRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;

    public void addMenuToCart(String username, Long menuId){
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(()->new RuntimeException("Menu Not Found"));

        CafeUser cafeUser = userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("User Not Found"));

        CartItem cartItem = cartRepository.findCartItemByCafeUserAndMenu(cafeUser,menu)
                .orElse(null);

        if(cartItem != null){
            cartItem.addQuantity();
            cartRepository.save(cartItem);
        }
        else {
            cartItem = new CartItem();
            cartItem.setMenu(menu);
            cartItem.setCafeUser(cafeUser);
            cartItem.setQuantity(1);
            cartRepository.save(cartItem);
        }
    }

    @Transactional
    public void placeOrder(String username) {
        CafeUser cafeUser = userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("User Not Found"));

        List<CartItem> cartItemList = cartRepository.findAllCartItemByCafeUser(cafeUser);

        List<OrderItem> orderItemList = new ArrayList<>();

        for(CartItem cart: cartItemList){
            OrderItem item = new OrderItem();
            item.setMenuTitle(cart.getMenu().getTitle());
            item.setPrice(cart.getMenu().getPrice());
            item.setQuantity(cart.getQuantity());
            orderItemList.add(item);
        }

        cartRepository.deleteByCafeUser(cafeUser);
        CafeOrder cafeOrder = new CafeOrder(cafeUser, OrderStatus.ORDERED);

        double totalPrice = 0.0;
        for(OrderItem item: orderItemList) {
            cafeOrder.addOrderItems(item);
            System.out.println("Order Items: " + cafeOrder.getOrderItemList().size());
        }
        orderRepository.save(cafeOrder);
    }

    public List<CartItem> getCart(String username) {
        CafeUser cafeUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return cartRepository.findAllCartItemByCafeUser(cafeUser);
    }


    public double calculateTotalAmount(String username){
        CafeUser cafeUser = userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("User Not Found"));

        List<CartItem> cartItems = cartRepository.findAllCartItemByCafeUser(cafeUser);

        double totalAmount = 0;
        for(CartItem cartItem: cartItems) {
            double itemPrice = cartItem.getMenu().getPrice();
            int quantity = cartItem.getQuantity();
            totalAmount += itemPrice * quantity;
        }

        return totalAmount;
    }

    public void updateQuantity(String username, Long menuId, String action){
        CafeUser cafeUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        CartItem item = cartRepository.findCartItemByCafeUserAndMenu(cafeUser, menuRepository.findById(menuId)
                        .orElseThrow(() -> new RuntimeException("Menu Not Found")))
                .orElse(null);

        if (item != null) {
            if ("increase".equals(action)) {
                item.setQuantity(item.getQuantity() + 1);
                cartRepository.save(item);
            } else if ("decrease".equals(action)) {
                int newQuantity = item.getQuantity() - 1;
                if (newQuantity <= 0) {
                    cartRepository.delete(item);
                } else {
                    item.setQuantity(newQuantity);
                    cartRepository.save(item);
                }
            }
        }
    }

    public int getCartQuantity(String username){
        CafeUser cafeUser = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User Not Found"));

        List<CartItem> cartItemList = cartRepository.findAllCartItemByCafeUser(cafeUser);

        return cartItemList.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}