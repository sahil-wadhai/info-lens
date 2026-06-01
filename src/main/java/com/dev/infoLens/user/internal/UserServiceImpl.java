package com.dev.infoLens.user.internal;

import com.dev.infoLens.common.exception.EmailAlreadyExistsException;
import com.dev.infoLens.common.exception.UserNotFoundException;
import com.dev.infoLens.common.exception.UsernameAlreadyExistsException;
import com.dev.infoLens.user.api.UserService;
import com.dev.infoLens.user.api.dto.UserResponseDTO;
import com.dev.infoLens.user.api.dto.UserRequestDTO;
import com.dev.infoLens.user.authApi.AuthUser;
import com.dev.infoLens.user.authApi.AuthUserProviderService;
import com.dev.infoLens.user.internal.model.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
class UserServiceImpl implements UserService, AuthUserProviderService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> UserMapper.toResponse(user))
                .toList(); // Java 16+ or use .collect(Collectors.toList())
    }


    public UserResponseDTO getUserById(Long id) {
        return UserMapper.toResponse(userRepository.findById(id).orElseThrow(()->{
            return new UserNotFoundException("User with ID " + id + " not found");
        }));
    }

    public UserResponseDTO getUserByUsername(String username) {
        return UserMapper.toResponse(userRepository.findByUsername(username).orElseThrow(()->{
            return new UserNotFoundException("User with username :" + username + " not found");
        }));
    }

    public UserResponseDTO saveUser(UserRequestDTO userRequest) {

        //userRequest.setPassword( passwordEncoder.encode(userRequest.getPassword()) ) ;
        User user = UserMapper.toEntity(userRequest);

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Username '" + user.getUsername() + "' is already taken");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email '" + user.getEmail() + "' is already registered");
        }
        return UserMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(String username) {
        long deletedCount = userRepository.deleteByUsername(username);
        if (deletedCount == 0) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }
    }

    @Override
    @Cacheable(value = "userIds", key = "#username")
    public Long getUserIdByUsername(String username) {
        return userRepository.findIdByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: "+username));
    }

    @Override
    public String getUserName(Long userID) {
        return userRepository.findUserNameByID(userID)
                .orElseThrow(() -> new UserNotFoundException("User not found: "+userID));
    }

    @Override
    public AuthUser findAuthUser(String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    AuthUser authUser = new AuthUser();
                    authUser.setUsername(user.getUsername());
                    authUser.setPasswordHash(user.getPassword());
                    authUser.setRole(user.getRole());
                    return authUser;
                })
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

    }
}


/*
    Even if Spring applies @Transactional internally for some methods:
    You should explicitly annotate service methods performing write operations —
    it makes the code behavior clear, and gives you more control (e.g., for rollback, propagation, isolation level).
*/
