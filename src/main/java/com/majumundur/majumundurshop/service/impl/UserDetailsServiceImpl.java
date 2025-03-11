package com.majumundur.majumundurshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.majumundur.majumundurshop.entity.UserAccount;
import com.majumundur.majumundurshop.repository.UserAccountRepository;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        return new User(
                userAccount.getUsername(),
                userAccount.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(userAccount.getRole().name()))
        );
    }
}
