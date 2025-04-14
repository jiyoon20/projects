package me.jooie.minicafe.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.jooie.minicafe.repository.CartRepository;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne
    @JoinColumn(name="menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name="cafe_user_id")
    private CafeUser cafeUser;

    @Column(nullable = false)
    private Integer quantity=0;

    public CartItem(CafeOrder cafeOrder, Menu menu, int quantity, CafeUser cafeUser) {
        this.menu = menu;
        this.quantity = quantity;
        this.cafeUser = cafeUser;
    }

    public void addQuantity(){
        this.quantity += 1;
    }

    public void reset(){
        this.quantity = 0;
    }
}