package me.jooie.minicafe.repository;

import me.jooie.minicafe.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

}
