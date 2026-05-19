package com.epam.hibernate.dto;

public record CredentialsResponse(
        String username,
        String password
) {}