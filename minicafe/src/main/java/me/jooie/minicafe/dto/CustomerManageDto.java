package me.jooie.minicafe.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Schema(description = "Customer Management Data Transfer Object")
public class CustomerManageDto {

    @Schema(description = "Unique identifier of the customer")
    Long userId;

    @Schema(description = "Name of the customer")
    String username;

    @Schema(description = "Email of the customer")
    String email;

    @Schema(description = "Time the customer registered at")
    LocalDateTime createdAt;

    @Schema(description = "Order number of the customer")
    Integer orderNumber;

    @Schema(description = "Total Price of the customer")
    Double totalPrice;

    @Schema(description = "Status of the customer (active/inactive)")
    Boolean active;
}
