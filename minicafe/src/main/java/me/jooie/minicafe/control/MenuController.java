package me.jooie.minicafe.control;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jooie.minicafe.domain.Menu;
import me.jooie.minicafe.dto.MenuCreateDto;
import me.jooie.minicafe.service.MenuService;
import me.jooie.minicafe.service.S3Service;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@Tag(name = "Menu", description = "Operations related to menu management")
public class MenuController {

    private final MenuService menuService;
    private final S3Service s3Service;

    @Operation(summary = "Show menu list page",
            description = "Displays all available menu items")
    @GetMapping("/menu/list")
    public String listMenu(Model model){
        List<Menu> menuList = menuService.getAllMenus();
        model.addAttribute("menuList",menuList);
        return "menu_list";
    }

    @Operation(summary = "Show menu detail page",
            description = "Displays a detailed page for a specific menu item")
    @GetMapping(value="/menu/detail/{menuId}")
    public String detail(Model model, @PathVariable("menuId") Long id) {
        model.addAttribute("menu", menuService.getMenu(id));
        return "menu_detail";
    }

    @Operation(summary = "admin: Show menu creation form",
            description = "Displays a form to create a new menu item")
    @GetMapping(value = "/menu/create")
    public String createMenuForm(Model model) {
        List<String> imageList = s3Service.getAllImageUrls();
        model.addAttribute("imageList", imageList);
        model.addAttribute("defaultImageUrl", s3Service.getDefaultImageUrl());
        model.addAttribute("menuCreateDto", new MenuCreateDto());
        return "menu_form";
    }

    @Operation(summary = "admin: Create a new menu item",
            description = "Registers a menu")
    @PostMapping(value = "/menu/create")
    public String createMenu(@Valid @ModelAttribute MenuCreateDto menuCreateDto,
                             BindingResult bindingResult,
                             @RequestParam("selectedImage") String selectedImage) {

        if (bindingResult.hasErrors()) {
            return "menu_form";
        }

        if (!selectedImage.isEmpty()) {
            menuCreateDto.setCoffeeImageUrl(selectedImage);
        } else {
            menuCreateDto.setCoffeeImageUrl(null);
        }
        menuService.createMenu(menuCreateDto);
        return "redirect:/menu/list";
    }

    @Operation(summary = "admin: Upload menu image to S3",
            description = "Handles image upload and returns S3 URL")
    @PostMapping("/admin/upload/images")
    @ResponseBody
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file){
        String fileName = file.getOriginalFilename();
        String url= s3Service.uploadFile(file,fileName);

        Map<String, String> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("url", url);
        return response;
    }

    @Operation(summary = "admin: Show menu modification form",
            description = "Displays a form to edit an existing menu item")
    @GetMapping("/menu/modify/{menuId}")
    public String showModifyForm(@PathVariable("menuId") Long menuId,
                                 Model model){
        Menu menu = menuService.findById(menuId);
        MenuCreateDto menuCreateDto = new MenuCreateDto();

        menuCreateDto.setMenuId(menu.getMenuId());
        menuCreateDto.setTitle(menu.getTitle());
        menuCreateDto.setPrice(menu.getPrice());
        menuCreateDto.setContent(menu.getContent());
        menuCreateDto.setCalories(menu.getCalories());
        menuCreateDto.setCoffeeImageUrl(menu.getCoffeeImageUrl());

        model.addAttribute("menuCreateDto",menuCreateDto);

        List<String> imageList = s3Service.getAllImageUrls();
        model.addAttribute("imageList", imageList);
        model.addAttribute("defaultImageUrl",s3Service.getDefaultImageUrl());

        return "menu_form";
    }

    @Operation(summary = "admin: Modify an existing menu item",
            description = "Handles menu item updates")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/menu/modify/{menuId}")
    public String modifyMenu(@PathVariable("menuId") Long menuId,
                             @ModelAttribute MenuCreateDto menuCreateDto,
                             BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return "menu_form";

        menuService.updateMenu(menuId, menuCreateDto);
        return "redirect:/menu/list";
    }

    @Operation(summary = "admin: Delete a menu item",
            description = "Removes a menu item")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/menu/delete/{menuId}")
    public String menuDelete(@PathVariable("menuId")Long menuId){
        Menu menu = this.menuService.getMenu(menuId);
        this.menuService.deleteMenu(menuId);
        return "redirect:/menu/list";
    }
}