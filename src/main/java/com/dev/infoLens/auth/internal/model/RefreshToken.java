package com.dev.infoLens.auth.internal.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Data
public class RefreshToken {
    @Id
    private String token;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private Instant expiryDate;
}
