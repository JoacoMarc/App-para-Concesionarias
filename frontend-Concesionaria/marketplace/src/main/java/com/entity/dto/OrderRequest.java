package com.entity.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class OrderRequest {
    private UUID userId;
    private UUID cartId;
    private String status;
}
