package com.dev.infoLens.auth.internal.repo;



import com.dev.infoLens.auth.internal.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbRefreshTokenRepository extends JpaRepository<RefreshToken, String> {
}

