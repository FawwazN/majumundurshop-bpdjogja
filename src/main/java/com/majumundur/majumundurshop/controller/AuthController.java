package com.majumundur.majumundurshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.majumundur.majumundurshop.constant.ApiBash;
import com.majumundur.majumundurshop.constant.Constant;
import com.majumundur.majumundurshop.model.request.AuthRequest;
import com.majumundur.majumundurshop.model.response.AuthResponse;
import com.majumundur.majumundurshop.model.response.CommonResponse;
import com.majumundur.majumundurshop.model.response.LogoutResponse;
import com.majumundur.majumundurshop.model.response.RegisterResponse;
import com.majumundur.majumundurshop.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiBash.AUTH)
public class AuthController {

    private final AuthService authService;

  @PostMapping(ApiBash.REGISTER)
  @PreAuthorize("hasAnyRole('MERCHANT')")
    public ResponseEntity<CommonResponse<?>> registerMerchant(@RequestBody AuthRequest authRequest) {
      RegisterResponse registerResponse = authService.createMerchant(authRequest);

      CommonResponse<?> commonResponse = CommonResponse.builder()
              .statusCode(HttpStatus.OK.value())
              .message(Constant.SUCCESS_REGISTER)
              .data(registerResponse)
              .build();
      return ResponseEntity.status(HttpStatus.CREATED).body(commonResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<CommonResponse<AuthResponse>> login(@RequestBody AuthRequest authRequest) {
      AuthResponse authResponse = authService.login(authRequest);

      CommonResponse<AuthResponse> commonResponse = CommonResponse.<AuthResponse>builder()
              .statusCode(HttpStatus.OK.value())
              .message("Login Successful")
              .data(authResponse)
              .build();
      return  ResponseEntity.ok(commonResponse);
  }

  @PostMapping("/logout")
    public ResponseEntity<CommonResponse<LogoutResponse>> logout(HttpServletRequest request) {
     LogoutResponse logoutResponse = authService.logout(request);
     CommonResponse<LogoutResponse> response = CommonResponse.<LogoutResponse>builder()
             .statusCode(HttpStatus.OK.value())
             .message("Logout Succefully")
             .data(logoutResponse)
             .build();
      return ResponseEntity.ok(response);

  }






















}
