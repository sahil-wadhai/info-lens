package com.dev.infoLens.auth.internal.repo.impl;

import com.dev.infoLens.auth.internal.dto.RefreshTokenDTO;
import com.dev.infoLens.auth.internal.model.RefreshToken;
import com.dev.infoLens.auth.internal.repo.DbRefreshTokenRepository;
import com.dev.infoLens.auth.internal.repo.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@ConditionalOnProperty(name = "security.refresh-token.token-store", havingValue = "database", matchIfMissing = true)
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    private final DbRefreshTokenRepository jpaRepository;

    public RefreshTokenRepositoryImpl(@Qualifier("dbRefreshTokenRepository") DbRefreshTokenRepository jpaRepository){
        this.jpaRepository = jpaRepository;
    }

    @Override
    public RefreshTokenDTO save(RefreshTokenDTO token) {
        RefreshToken entity = new RefreshToken();
        entity.setToken(token.getToken());
        entity.setUsername(token.getUsername());
        entity.setExpiryDate(token.getExpiryDate());
        jpaRepository.save(entity);
        return token;
    }

    @Override
    public Optional<RefreshTokenDTO> findByToken(String token) {
        return jpaRepository.findById(token)
                .map(entity -> new RefreshTokenDTO(entity.getToken(), entity.getUsername(), entity.getExpiryDate()));
    }

    @Override
    public void deleteByToken(String token) {
        jpaRepository.deleteById(token);
    }
}

