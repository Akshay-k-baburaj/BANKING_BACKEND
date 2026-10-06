package com.learning.Bank.service;

import com.learning.Bank.dto.LoginRequest;
import com.learning.Bank.dto.LoginResponse;
import com.learning.Bank.dto.RegisterRequest;
import com.learning.Bank.dto.RegisterResponse;
import com.learning.Bank.entity.User;
import com.learning.Bank.entity.UserType;
import com.learning.Bank.exception.EmailAlreadyExistsException;
import com.learning.Bank.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager,JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email already exist try login in ");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        String password = passwordEncoder.encode(request.getPassword());
        user.setPassword(password);
        user.setUserType(UserType.CUSTOMER);
        User savedUser = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setUserType(savedUser.getUserType());
        return response;
    }

    public LoginResponse login(LoginRequest loginRequest){
        Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return new LoginResponse(token);
    }
}
