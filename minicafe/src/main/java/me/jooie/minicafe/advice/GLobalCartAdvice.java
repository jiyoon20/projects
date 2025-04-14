package me.jooie.minicafe.advice;

import jakarta.persistence.Column;
import me.jooie.minicafe.domain.CartItem;
import me.jooie.minicafe.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;

@ControllerAdvice
public class GLobalCartAdvice {
    @Autowired
    private CartService cartService;

    @ModelAttribute
    public void addCartQuantity(Model model, Principal principal){
        int cartQuantity = 0;
        if(principal != null) {
            String username = principal.getName();
            cartQuantity = cartService.getCartQuantity(username);
        }
        model.addAttribute("cartQuantity", cartQuantity);

    }
}
