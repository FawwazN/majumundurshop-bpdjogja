package com.majumundur.majumundurshop.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${majumundur.secretkey}")
    private  String SECRET_KEY;

    @Value("${majumundur.expiration}")
    private Long EXPIRATION_TIME;

    //mengekstrak username dari token jwt
    public String getUserNameFromToken(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        return decodedJWT.getSubject();
    }

    //validasi token JWT
    public Boolean validateToken(String token) {
        try{
            DecodedJWT decodedJWT = getDecodedJWT(token);
            return !decodedJWT.getExpiresAt().before(new Date());
        }catch (Exception e){
            return false;
        }
    }

    public String getRoleFromToken(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        String role = decodedJWT.getClaim("role").asString();

        if (role.startsWith("[") && role.endsWith("]")) {
            role = role.substring(1, role.length() - 1);
        }
        System.out.println("DEBUG ROLE : " + role );
        return role;
    }

    public String generateToken(String username , String role) {
        String token = JWT.create()
                .withSubject(username)
                .withClaim("role" , role)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET_KEY));
        return token;
    }

    public Long getExpirationTime(String token) {
        DecodedJWT decodedJWT = getDecodedJWT(token);
        Date expirationDate = decodedJWT.getExpiresAt();
        return expirationDate.getTime() - System.currentTimeMillis();
    }


    private DecodedJWT getDecodedJWT(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET_KEY))
                .build()
                .verify(token); // Verifikasi dan decode token
        return decodedJWT;
    }

}
