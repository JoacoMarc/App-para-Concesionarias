package com.entity.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class CategoryRequest {
    private UUID id;
    private String description;
}
