package com.dev.infoLens.user.internal;


import com.dev.infoLens.common.datatype.Role;
import com.dev.infoLens.user.api.dto.UserRequestDTO;
import com.dev.infoLens.user.api.dto.UserResponseDTO;
import com.dev.infoLens.user.internal.model.User;

class UserMapper {

    public static User toEntity(UserRequestDTO request) {
        return User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .role(Role.USER)
                .build();
    }

    public static UserResponseDTO toResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
