package me.jooie.minicafe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Schema(description = "DTO for creating an admin user")
public class AdminCreateDto {

    @Schema(description = "Name of the admin")
    @NotNull(message="Username is required!")
    private String username;

    @Schema(description = "Password of the admin")
    @NotEmpty(message="Password is required!")
    private String password1;

    @Schema(description = "Password confirmation of the admin")
    @NotEmpty(message="Password confirmation is required!")
    private String password2;

    @Schema(description = "Email of the admin")
    private String email;
}

