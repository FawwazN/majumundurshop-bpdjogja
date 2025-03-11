package com.majumundur.majumundurshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenBlacklistService {

    private final StringRedisTemplate stringRedisTemplate;

    //menyimpan token ke dalam redis dengan TTL(misal 24 jam)
    public void blackListToken(String token , Long expirationTIme) {
        stringRedisTemplate.opsForValue().set(token , "blacklisted" ,expirationTIme, TimeUnit.MILLISECONDS);
    }

    //mengecek apakah token sudah ada didalam redis (blacklist)
    public Boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(token));
    }
}
