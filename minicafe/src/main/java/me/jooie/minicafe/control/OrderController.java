package me.jooie.minicafe.control;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.jooie.minicafe.domain.CafeOrder;
import me.jooie.minicafe.domain.CartItem;
import me.jooie.minicafe.domain.Menu;
import me.jooie.minicafe.domain.OrderStatus;
import me.jooie.minicafe.service.CartService;

import me.jooie.minicafe.service.OrderService;
import me.jooie.minicafe.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@Tag(name = "Order",
        description = "Operations for order placement and management")
public class OrderController {

    private final CartService cartService;
    private final UserService userService;
    private final OrderService orderService;

    @Operation(summary = "Submit an order",
            description = "Places an order of the current user")
    @PostMapping("/order/submit")
    public String submitOrder(Principal principal){
        String username = principal.getName();
        cartService.placeOrder(username);

        return "redirect:/order/submit";
    }

    @Operation(summary = "Show order review page",
            description = "Displays order items after order submission")
    @GetMapping("/order/submit")
    public String reviewOrder(Model model, Principal principal){

        String username = principal.getName();
        CafeOrder latestOrder = orderService.getLatestOrder(username);

        model.addAttribute("orderItemList", latestOrder.getOrderItemList());
        model.addAttribute("totalAmount", latestOrder.getTotalAmount()); // 필요 시

        return "order_review";
    }

    @Operation(summary = "Admin: View all orders",
            description = "Shows all customer orders by paginated list")
    @GetMapping("/admin/order/list")
    public String showOrderList(Model model, @RequestParam(value="page", defaultValue="0") int page){
        Page<CafeOrder> paging = orderService.getList(page);

        paging.getContent().forEach(order -> {
            if (order.getOrderItemList() == null) {
                order.setOrderItemList(new ArrayList<>());
            }
        });

        model.addAttribute("paging", paging);
        return "admin/order_list";
    }

    @Operation(summary = "Admin: Update order status",
            description = "Allows admin to update the status of an order")
    @PostMapping("/admin/order/status")
    public String updateOrderStatus(@RequestParam Long orderId, @RequestParam OrderStatus orderStatus) {
        orderService.updateOrderStatus(orderId, orderStatus);
        return "redirect:/admin/order/list";
    }

}
