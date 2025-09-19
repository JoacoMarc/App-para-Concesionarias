package com.entity.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreateOrderFromCartRequest {
    
    @NotBlank(message = "La dirección de envío es obligatoria")
    private String shippingAddress;
    
    @NotBlank(message = "La dirección de facturación es obligatoria")
    private String billingAddress;
    
    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethod;
    
    @NotNull(message = "El estado de pago es obligatorio")
    private Boolean isPaid = false;
}