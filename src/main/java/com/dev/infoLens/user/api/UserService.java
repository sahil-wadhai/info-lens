package com.dev.infoLens.user.api;

import com.dev.infoLens.user.api.dto.UserRequestDTO;
import com.dev.infoLens.user.api.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserByUsername(String username);

    UserResponseDTO saveUser(UserRequestDTO user);

    void deleteUser(String username);

    Long getUserIdByUsername(String username);

    String getUserName(Long userId);
}
