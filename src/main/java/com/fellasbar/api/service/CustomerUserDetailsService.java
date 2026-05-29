package com.fellasbar.api.service;

import com.fellasbar.api.model.Customer;
import com.fellasbar.api.repository.CustomerRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomerUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("No account found for " + email));
        if (!customer.hasPassword()) {
            throw new UsernameNotFoundException("No password set for " + email);
        }
        return new User(customer.getEmail(), customer.getPasswordHash(),
            List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
    }
}
