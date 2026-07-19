package com.dev.infoLens.auth.internal.repo;

import com.dev.infoLens.auth.internal.dto.RefreshTokenDTO;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshTokenDTO save(RefreshTokenDTO token);
    Optional<RefreshTokenDTO> findByToken(String token);
    void deleteByToken(String token);
}

