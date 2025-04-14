package me.jooie.minicafe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "DTO for creating or updating menu items")
public class MenuCreateDto {

    @Schema(description = "Unique Identifier of the menu item")
    private Long menuId;

    @Schema(description = "Url for the coffee Image file")
    private String coffeeImageUrl;

    @Schema(description = "Name of the menu item")
    @NotBlank(message ="title is required!")
    private String title;

    @Schema(description = "Price of the menu item")
    @NotNull(message="price is required!")
    @Min(value=0, message = "price should be over $0.0.")
    private Double price;

    @Schema(description = "Description of the menu item")
    private String content;

    @Schema(description = "Calories of the menu item")
    private Double calories;
}
