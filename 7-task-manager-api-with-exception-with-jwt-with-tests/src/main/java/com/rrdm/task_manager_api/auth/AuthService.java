package com.rrdm.task_manager_api.auth;

import com.rrdm.task_manager_api.dto.auth.AuthResponse;
import com.rrdm.task_manager_api.dto.auth.LoginRequest;
import com.rrdm.task_manager_api.dto.auth.RegisterRequest;
import com.rrdm.task_manager_api.exceptions.EmailAlreadyExistsException;
import com.rrdm.task_manager_api.security.JwtUtil;
import com.rrdm.task_manager_api.users.User;
import com.rrdm.task_manager_api.users.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request){
        User user = new User();
        boolean isEmailExists = userRepository.existsUserByEmail(request.getEmail());
        if(isEmailExists) throw new EmailAlreadyExistsException("Email already exists");
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); //hashed password
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getUsername());
    }

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtUtil.generateToken(request.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getUsername());
    }


}
