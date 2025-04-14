package me.jooie.minicafe.repository;

import me.jooie.minicafe.domain.CafeUser;
import me.jooie.minicafe.domain.CartItem;
import me.jooie.minicafe.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllCartItemByCafeUser(CafeUser cafeUser);
    Optional<CartItem> findCartItemByCafeUserAndMenu(CafeUser cafeUser, Menu menu);

    @Modifying
    @Transactional
    void deleteByCafeUser(CafeUser cafeUser);
}
