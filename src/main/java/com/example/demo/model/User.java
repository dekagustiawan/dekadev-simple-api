package com.example.demo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users") // <-- change from "user" to "users"
public class User {
  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @NotBlank
    @Schema(description = "Full name of the user", example = "Jane Doe")
    private String name;

    @Email
    @NotBlank
    @Schema(description = "Email address", example = "jane.doe@example.com")
    private String email;
}
