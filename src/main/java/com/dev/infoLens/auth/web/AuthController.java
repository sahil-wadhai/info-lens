package com.dev.infoLens.auth.web;


import com.dev.infoLens.auth.internal.dto.RefreshTokenDTO;
import com.dev.infoLens.auth.internal.service.JwtService;
import com.dev.infoLens.auth.internal.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;

    @Value("${security.refresh-token.expiration:604800000}") // Default 7 days in milliseconds
    private long refreshTokenExpiration;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        String token = jwtService.generateToken(
                authentication.getName(),
                authentication.getAuthorities()
        );
        RefreshTokenDTO refreshToken = refreshTokenService.createRefreshToken(authentication.getName());
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(true) // Set to false during local HTTP testing
                .path("/api/auth/refresh") // Cookie only sent to the refresh endpoint
                .maxAge(refreshTokenExpiration/1000) // 7 days matching token lifespan
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        return new ResponseEntity<>(new AuthenticationResponse("success", token), HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshAccess(HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new RuntimeException("Refresh token is missing");
        }

        String requestRefreshToken = Arrays.stream(cookies)
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .map(cookie -> cookie.getValue())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Refresh token is missing"));

        // 2. Validate token from db
        RefreshTokenDTO token = refreshTokenService.findByToken(requestRefreshToken);
        refreshTokenService.verifyExpiration(token);

        // 3. Rotate tokens (Delete old refresh token)
        refreshTokenService.deleteByToken(requestRefreshToken);
        RefreshTokenDTO newRefreshToken = refreshTokenService.createRefreshToken(token.getUsername());

        // 4. Set new cookie
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", newRefreshToken.getToken())
                .httpOnly(true)
                .secure(true) // Set to false during local HTTP testing
                .path("/api/auth/refresh") // Cookie only sent to the refresh endpoint
                .maxAge(refreshTokenExpiration/1000) // 7 days matching token lifespan
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // 5. Generate new Access token
        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getUsername());

        String newAccessToken = jwtService.generateToken(token.getUsername(),userDetails.getAuthorities());
        return new ResponseEntity<>(new AuthenticationResponse("refreshed", newAccessToken), HttpStatus.OK);
    }
}





