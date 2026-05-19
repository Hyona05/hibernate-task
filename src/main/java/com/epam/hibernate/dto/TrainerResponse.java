package com.epam.hibernate.dto;

public record TrainerResponse(
        String username,
        String firstName,
        String lastName,
        String specialization,
        Boolean isActive
) {}