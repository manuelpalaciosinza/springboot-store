package com.codewithmosh.store.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UsersMapper usersMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.usersMapper = usersMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public Iterable<UserDto> getAllUsers(String sortBy) {
        if (!Set.of("name", "email").contains(sortBy))
            sortBy = "name";


        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(usersMapper::toDto)
                .toList();

    }

    public UserDto getUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return usersMapper.toDto(user);
    }

    public UserDto registerUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException();
        }

        var user = usersMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        return usersMapper.toDto(user);
    }

    public UserDto updateUser(Long userId, UpdateUserRequest request) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        usersMapper.update(request, user);
        userRepository.save(user);

        return usersMapper.toDto(user);
    }

    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        var user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AccessDeniedException("Password does not match");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }
}
