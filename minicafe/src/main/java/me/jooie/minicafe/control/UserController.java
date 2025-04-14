package me.jooie.minicafe.control;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.jooie.minicafe.domain.UserRole;
import me.jooie.minicafe.dto.UserCreateDto;
import me.jooie.minicafe.service.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@Tag(name = "User", description = "Operations for User registration and login")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Show user signup form",
            description = "Displays the user signup form")
    @GetMapping("/user/signup")
    public String showUserForm(Model model){
        model.addAttribute("userCreateDto", new UserCreateDto());
        return "user_signup_form";
    }

    @Operation(summary = "Register a user",
            description = "Creates a new user")
    @PostMapping("/user/signup")
    public String userSignup(@Valid UserCreateDto userCreateDto,
                             BindingResult bindingResult){

        if(bindingResult.hasErrors())
            return "user_signup_form";

        if(!userCreateDto.getPassword1().equals(userCreateDto.getPassword2())){
            bindingResult.rejectValue("password2","passwordIncorrect",
                    "Passwords are not equal.");
            return "user_signup_form";
        }

        try{
            userService.createUser(userCreateDto.getUsername(),
                    userCreateDto.getPassword1(),userCreateDto.getEmail(),
                   UserRole.USER);
        } catch(DataIntegrityViolationException e){
            e.printStackTrace();
            bindingResult.reject("signupFailed","This user id already exists.");
            return "user_signup_form";
        } catch(Exception e){
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "user_signup_form";
        }
        return "redirect:/menu/list";
    }

    @Operation(summary = "Show login form",
            description = "Displays the user login form")
    @GetMapping("/user/login")
    public String showLoginForm(){
        return "user_login_form";
    }
}
