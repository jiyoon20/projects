package me.jooie.minicafe.control;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.jooie.minicafe.domain.CafeUser;
import me.jooie.minicafe.domain.Menu;
import me.jooie.minicafe.domain.UserRole;
import me.jooie.minicafe.dto.AdminCreateDto;
import me.jooie.minicafe.dto.CustomerManageDto;
import me.jooie.minicafe.dto.MenuCreateDto;
import me.jooie.minicafe.dto.UserCreateDto;
import me.jooie.minicafe.service.UserService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
@Tag(name = "Admin", description = "operations for only administrator")
public class AdminController {
    private final UserService userService;

    @Operation(summary = "Show admin landing page",
            description = "Displays the entry page for admin." +
                    "Shows whether an admin exists and whether the current user is an admin.")
    @GetMapping("/")
    public String showAdminEntry(Model model, Principal principal){
        boolean isAdmin = false;
        boolean adminExists = userService.existsAdmin("ROLE_ADMIN");

        if(principal != null) {
            CafeUser user = userService.getUserByUsername(principal.getName());
            isAdmin = user.getRole().equals("ROLE_ADMIN");
        }
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("adminExists",adminExists);
        return "admin/index";
    }

    @Operation(summary = "Show admin signup form",
            description = "Displays a creation form for admin.")
    @GetMapping("/signup")
    public String showAdminForm(Model model){
        model.addAttribute("adminCreateDto",new AdminCreateDto());
        return "admin/admin_signup_form";
    }

    @Operation(summary = "Register a new admin",
            description = "Handles registration of a new admin")
    @PostMapping("/signup")
    public String adminSignup(@Valid AdminCreateDto adminCreateDto,
                              BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "admin/admin_signup_form";

        if(!adminCreateDto.getPassword1().equals(adminCreateDto.getPassword2())){
            bindingResult.rejectValue("password2","passwordIncorrect",
                    "Passwords are not equal.");
            return "admin/admin_signup_form";
        }

        try{
            userService.createUser(adminCreateDto.getUsername(),
                    adminCreateDto.getPassword1(),
                    adminCreateDto.getEmail(),
                    UserRole.ADMIN);
        } catch(DataIntegrityViolationException e){
            bindingResult.reject("signupFailed","This admin id already exists");
            return "admin/admin_signup_form";
        } catch (Exception e) {
            bindingResult.reject("signupFailed",e.getMessage());
            return "admin/admin_signup_form";
        }

        return "admin/admin_main";
    }

    @Operation(summary = "Show admin dashboard",
            description = "Displays the admin dashboard page.")
    @GetMapping("/main")
    public String showAdminMain(Model model)
    {
        return "admin/admin_main";

    }

    @Operation(summary = "Get customer list",
            description = "Returns list of customers.")
    @GetMapping("/customers")
    public String getCustomerList(Model model) {
        List<CustomerManageDto> customerManageDtos = userService.getCustomerManageList();
        model.addAttribute("customerManageDtos",customerManageDtos);
        return "admin/customer_list";
    }

    @Operation(summary = "Toggle user activation status",
            description = "Decides to enable or disable a customer's account.")
    @PostMapping("/customers/{userId}/toggle-active")
    public String toggleUserActivation(@PathVariable("userId") Long userId){
        userService.toggleActivation(userId);
        return "redirect:/admin/customers";
    }
}
