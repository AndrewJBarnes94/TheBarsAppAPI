package com.fellasbar.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${fellasbar.admin.username}")
    private String adminUsername;

    @Value("${fellasbar.admin.password}")
    private String adminPassword;

    @Value("${fellasbar.users.aj.password}")
    private String ajPassword;

    @Value("${fellasbar.users.bill.password}")
    private String billPassword;

    @Value("${fellasbar.users.dom.password}")
    private String domPassword;

    @Value("${fellasbar.users.seth.password}")
    private String sethPassword;

    @Value("${fellasbar.users.trevor.password}")
    private String trevorPassword;

    @Value("${fellasbar.users.jared.password}")
    private String jaredPassword;

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**", "/h2-console/**")
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**").permitAll()
                .requestMatchers("/admin/**").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/admin", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var admin = User.builder()
            .username(adminUsername)
            .password(passwordEncoder().encode(adminPassword))
            .roles("ADMIN")
            .build();
        var aj = User.builder()
            .username("aj")
            .password(passwordEncoder().encode(ajPassword))
            .roles("ADMIN")
            .build();
        var bill = User.builder()
            .username("bill")
            .password(passwordEncoder().encode(billPassword))
            .roles("ADMIN")
            .build();
        var dom = User.builder()
            .username("dom")
            .password(passwordEncoder().encode(domPassword))
            .roles("ADMIN")
            .build();
        var seth = User.builder()
            .username("seth")
            .password(passwordEncoder().encode(sethPassword))
            .roles("ADMIN")
            .build();
        var trevor = User.builder()
            .username("trevor")
            .password(passwordEncoder().encode(trevorPassword))
            .roles("ADMIN")
            .build();
        var jared = User.builder()
            .username("jared")
            .password(passwordEncoder().encode(jaredPassword))
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(admin, aj, bill, dom, seth, trevor, jared);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
