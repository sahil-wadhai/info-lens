package com.dev.infoLens.user.authApi;

import com.dev.infoLens.common.datatype.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthUser{
    private String username;
    private String passwordHash;
    private Role role;
}
