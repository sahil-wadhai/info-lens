package com.dev.infoLens.auth.internal.service;

import com.dev.infoLens.auth.internal.dto.RefreshTokenDTO;
import com.dev.infoLens.auth.internal.repo.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${security.refresh-token.expiration}")
    private long refreshExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenDTO createRefreshToken(String username) {
        RefreshTokenDTO refreshTokenDTO = RefreshTokenDTO.builder()
                .token(UUID.randomUUID().toString())
                .username(username)
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();

        return refreshTokenRepository.save(refreshTokenDTO);
    }

    public RefreshTokenDTO verifyExpiration(RefreshTokenDTO token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.deleteByToken(token.getToken());
            throw new RuntimeException("Refresh token expired. Please log in again.");
        }
        return token;
    }

    public RefreshTokenDTO findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found in storage."));
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}

