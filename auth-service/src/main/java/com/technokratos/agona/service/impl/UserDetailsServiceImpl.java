package com.technokratos.agona.service.impl;

import com.technokratos.agona.dto.UserDetailsImpl;
import com.technokratos.agona.entity.UserAccount;
import com.technokratos.agona.exception.authentication.UserAccountNotFoundException;
import com.technokratos.agona.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccount user = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new UserAccountNotFoundException("email", email));

        return new UserDetailsImpl(user);
    }
}
