package me.jooie.minicafe.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.jooie.minicafe.DataNotFoundException;
import me.jooie.minicafe.domain.Menu;
import me.jooie.minicafe.dto.MenuCreateDto;
import me.jooie.minicafe.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public List<Menu> getList() {
        return this.menuRepository.findAll();
    }

    public Menu getMenu(Long menuId){
       return menuRepository.findById(menuId)
               .orElseThrow(()->new DataNotFoundException("Menu Not Found"));
    }

    public void createMenu(MenuCreateDto menuCreateDto){
        Menu menu = new Menu();

        menu.setCoffeeImageUrl(menuCreateDto.getCoffeeImageUrl());
        menu.setTitle(menuCreateDto.getTitle());
        menu.setPrice(menuCreateDto.getPrice());
        menu.setContent(menuCreateDto.getContent());
        menu.setCalories(menuCreateDto.getCalories());

        menuRepository.save(menu);
    }

    public void updateMenu(Long menuId, MenuCreateDto menuCreateDto){
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(()-> new DataNotFoundException("Menu Not Found"));

        menu.setCoffeeImageUrl(menuCreateDto.getCoffeeImageUrl());
        menu.setTitle(menuCreateDto.getTitle());
        menu.setPrice(menuCreateDto.getPrice());
        menu.setContent(menuCreateDto.getContent());
        menu.setCalories(menuCreateDto.getCalories());

        menuRepository.save(menu);
    }

    public List<Menu> getAllMenus() {
        List<Menu> menuList = menuRepository.findAll();
        return menuList;
    }

    public Menu findById(Long menuId){
        return menuRepository.findById(menuId)
                .orElseThrow(()-> new RuntimeException("Menu Not Found"));
    }

    public void save(Menu menu){
        menuRepository.save(menu);
    }

    public void deleteMenu(Long menuId){
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(()->new DataNotFoundException("Menu Not Found"));

        menuRepository.delete(menu);
    }
}