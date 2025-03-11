package com.majumundur.majumundurshop.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.majumundur.majumundurshop.constant.Constant;
import com.majumundur.majumundurshop.constant.UserRole;
import com.majumundur.majumundurshop.entity.UserAccount;
import com.majumundur.majumundurshop.model.request.AuthRequest;
import com.majumundur.majumundurshop.model.response.AuthResponse;
import com.majumundur.majumundurshop.model.response.LogoutResponse;
import com.majumundur.majumundurshop.model.response.RegisterResponse;
import com.majumundur.majumundurshop.repository.UserAccountRepository;
import com.majumundur.majumundurshop.security.JwtAuthenticationFilter;
import com.majumundur.majumundurshop.security.JwtTokenProvider;
import com.majumundur.majumundurshop.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepository userAccountRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTokenBlacklistService redisTokenBlacklistService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationManager authenticationManager;

    @Override
    public RegisterResponse createMerchant(AuthRequest authRequest) {
        try {
            UserRole userRole = UserRole.ROLE_MERCHANT;

            UserAccount userAccount = UserAccount.builder()
                    .username(authRequest.getUsername())
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .role(userRole)
                    .build();
            userAccountRepository.saveAndFlush(userAccount);
            return toRegisterResponse(userAccount);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT , Constant.USERNAME_DUPLICATE);
        }
    }

    @Override
    public AuthResponse login(AuthRequest authRequest) {

        //GUnakan AuthenticationManager untuk authentikasi user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        // membuat jwt token dari hasil username dan password
        String token = jwtTokenProvider.generateToken(user.getUsername(),user.getAuthorities().toString());

        // kembalikan hasil authresponse yang berisi token dan role
        return AuthResponse.builder()
                .accessToken(token)
                .role(user.getAuthorities().toString())
                .build();
    }

    @Override
    public LogoutResponse logout(HttpServletRequest request) {
        String token = jwtAuthenticationFilter.extractTokenFromRequest(request);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Token");
        }

        Long expirationTime = jwtTokenProvider.getExpirationTime(token);
        redisTokenBlacklistService.blackListToken(token,expirationTime);

        return LogoutResponse.builder()
                .message("Logout Successfully")
                .token(token)
                .expiredIn(expirationTime)
                .build();
    }

    private static RegisterResponse toRegisterResponse(UserAccount userAccount) {
        return RegisterResponse.builder()
                .id(userAccount.getId())
                .username(userAccount.getUsername())
                .role(userAccount.getRole().getDescription())
                .build();
    }
}
