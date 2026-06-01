package com.dev.infoLens.user.api.dto;

import com.dev.infoLens.common.datatype.Role;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String username;
    private String email;
    private Role role;
}
