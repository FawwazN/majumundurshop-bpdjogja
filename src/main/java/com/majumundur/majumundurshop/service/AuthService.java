package com.majumundur.majumundurshop.service;

import com.majumundur.majumundurshop.model.request.AuthRequest;
import com.majumundur.majumundurshop.model.response.AuthResponse;
import com.majumundur.majumundurshop.model.response.LogoutResponse;
import com.majumundur.majumundurshop.model.response.RegisterResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    RegisterResponse createMerchant (AuthRequest authRequest);
    AuthResponse login (AuthRequest authRequest);
    LogoutResponse logout( HttpServletRequest request);
}
