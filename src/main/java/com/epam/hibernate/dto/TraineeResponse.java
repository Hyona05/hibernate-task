package com.epam.hibernate.dto;

import java.time.LocalDate;

public record TraineeResponse(
        String username,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String address,
        Boolean isActive
) {}