package com.fellasbar.api.service;

import com.fellasbar.api.repository.BusinessUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BusinessUserDetailsService implements UserDetailsService {

    private final BusinessUserRepository businessUserRepository;

    public BusinessUserDetailsService(BusinessUserRepository businessUserRepository) {
        this.businessUserRepository = businessUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = businessUserRepository.findByEmail(email)
            .filter(u -> u.isActive())
            .orElseThrow(() -> new UsernameNotFoundException("No active business user: " + email));

        return User.builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .roles("BUSINESS")
            .build();
    }
}
