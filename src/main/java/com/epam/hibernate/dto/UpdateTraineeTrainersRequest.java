package com.epam.hibernate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UpdateTraineeTrainersRequest(
        @NotBlank String authUsername,
        @NotBlank String password,
        @NotBlank String traineeUsername,
        @NotEmpty List<String> trainerUsernames
) {}