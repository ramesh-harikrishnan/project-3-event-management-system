package com.example.project_three_event_management_system.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class SignupRequest {

    @NotBlank
    @Schema(description = "User name")
    private String name;

    @NotBlank
    @Email
    @Schema(description = "User email")
    private String email;

    @NotBlank
    @Schema(description = "User password")
    private String password;

    @Schema(description = "User role")
    private String role;
}
