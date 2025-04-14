package me.jooie.minicafe.repository;

import me.jooie.minicafe.domain.CafeUser;
import me.jooie.minicafe.domain.CafeOrder;
import me.jooie.minicafe.domain.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<CafeOrder, Long> {
    Optional<CafeOrder> findByCafeUserAndOrderStatus(CafeUser cafeUser, OrderStatus orderStatus);
    int countByCafeUser(CafeUser cafeUser);
    List<CafeOrder> findByCafeUser(CafeUser cafeUser);

    @EntityGraph(attributePaths = {"orderItemList", "cafeUser"})
    Page<CafeOrder> findAll(Pageable pageable);

    Optional<CafeOrder> findTopByCafeUserOrderByOrderIdDesc(CafeUser cafeUser);
}
