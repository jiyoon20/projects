package me.jooie.minicafe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@RequiredArgsConstructor
@Schema(description = "DTO for creating an user")
public class UserCreateDto {

    @Schema(description = "Name of the user")
    @NotNull(message="Username is required!")
    private String username;

    @Schema(description = "Password of the user")
    @NotEmpty(message="Password is required!")
    private String password1;

    @Schema(description = "Password confirmation of the user")
    @NotEmpty(message="Password confirmation is required!")
    private String password2;

    @Schema(description = "Email of the user")
    private String email;
}
