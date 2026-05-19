package com.epam.hibernate.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTrainerRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String specialization
) {}