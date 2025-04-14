package me.jooie.minicafe.control;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.jooie.minicafe.domain.CartItem;
import me.jooie.minicafe.service.CartService;
import org.apache.catalina.mbeans.SparseUserDatabaseMBean;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RequiredArgsConstructor
@Controller
@Tag(name = "Cart", description = "Operations related with cart management")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Add item to cart",
            description = "Puts a menu item into the cart for the logged-in user.")
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("menuId") Long menuId,
                            Principal principal){

        if(principal == null)
            return "redirect:/user/login";

        String username = principal.getName();
        cartService.addMenuToCart(username,menuId);
        return "redirect:/cart/view";
    }

    @Operation(summary = "Show cart",
            description = "Displays the contents of the cart for the logged-in user.")
    @GetMapping("/cart/view")
    public String viewCart(Model model, Principal principal, HttpSession session){
        if(principal == null)
            return "redirect:/user/login";

        String username=principal.getName();
        List<CartItem> cartList = cartService.getCart(username);

        session.setAttribute("cartlist", cartList);

        double totalAmount = cartService.calculateTotalAmount(username);

        model.addAttribute("cartlist", cartList);
        model.addAttribute("totalAmount", totalAmount);
        return "cart_view";
    }

    @Operation(summary = "Update cart item quantity",
            description = "Increases or decreases the quantity of an item in the cart.")
    @PostMapping("/cart/update")
    public String updateCart(@RequestParam Long menuId,
                             @RequestParam String action,
                             Principal principal) {

        if (principal == null) {
                return "redirect:/cart/view";
        }
        String username = principal.getName();
        cartService.updateQuantity(username, menuId, action);
        return "redirect:/cart/view";
    }
}
