package com.datapar.ecommerce.User;

import com.datapar.ecommerce.Exceptions.AppException;
import com.datapar.ecommerce.Mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    public UserDTO login(CredentialsDTO credentialsDTO) {
        User user = userRepository.findByEmail(credentialsDTO.email())
                .orElseThrow(() -> new AppException("Credenciales invalidas", HttpStatus.UNAUTHORIZED));

        if (passwordEncoder.matches(CharBuffer.wrap(credentialsDTO.password()), user.getPassword())) {
            return userMapper.userToUserDTO(user);
        }

        throw new AppException("Credenciales invalidas", HttpStatus.UNAUTHORIZED);
    }

    public UserDTO register(RegisterDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(userDTO.email());

        if (optionalUser.isPresent()) {
            throw new AppException("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.registerDTOToUser(userDTO);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDTO.password())));

        User savedUser = userRepository.save(user);

        return userMapper.userToUserDTO(savedUser);
    }

    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Credenciales invalidas", HttpStatus.UNAUTHORIZED));
        return userMapper.userToUserDTO(user);
    }
}
