package com.epam.hibernate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AddTrainingRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String traineeUsername,
        @NotBlank String trainerUsername,
        @NotBlank String trainingName,
        @NotBlank String trainingTypeName,
        @NotNull LocalDate trainingDate,
        @NotNull Integer trainingDuration
) {}