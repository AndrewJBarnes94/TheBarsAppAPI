package com.fellasbar.api.config;

import com.fellasbar.api.service.BusinessUserDetailsService;
import com.fellasbar.api.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${fellasbar.rememberme.key}")
    private String rememberMeKey;

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
            .securityMatcher("/api/**", "/h2-console/**", "/webhooks/**")
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain portalFilterChain(HttpSecurity http,
                                                  BusinessUserDetailsService businessUserDetailsService) throws Exception {
        http
            .securityMatcher("/portal/**")
            .userDetailsService(businessUserDetailsService)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/portal/login").permitAll()
                .anyRequest().hasRole("BUSINESS")
            )
            .formLogin(form -> form
                .loginPage("/portal/login")
                .defaultSuccessUrl("/portal/dashboard", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/portal/logout")
                .logoutSuccessUrl("/portal/login?logout")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain storeFilterChain(HttpSecurity http,
                                                 CustomerUserDetailsService customerUserDetailsService) throws Exception {
        InMemoryUserDetailsManager fellaManager = new InMemoryUserDetailsManager(
            User.builder().username("aj").password(passwordEncoder().encode(ajPassword)).roles("USER").build(),
            User.builder().username("bill").password(passwordEncoder().encode(billPassword)).roles("USER").build(),
            User.builder().username("dom").password(passwordEncoder().encode(domPassword)).roles("USER").build(),
            User.builder().username("seth").password(passwordEncoder().encode(sethPassword)).roles("USER").build(),
            User.builder().username("trevor").password(passwordEncoder().encode(trevorPassword)).roles("USER").build(),
            User.builder().username("jared").password(passwordEncoder().encode(jaredPassword)).roles("USER").build()
        );
        UserDetailsService storeUserDetailsService = username -> {
            try {
                return customerUserDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                return fellaManager.loadUserByUsername(username);
            }
        };

        http
            .securityMatcher("/store/**")
            .userDetailsService(storeUserDetailsService)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/store", "/store/success", "/store/save-cart",
                    "/store/login", "/store/register",
                    "/store/initiate-checkout", "/store/set-password",
                    "/store/forgot-password", "/store/reset-password").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/store/login")
                .defaultSuccessUrl("/store/initiate-checkout", true)
                .permitAll()
            )
            .rememberMe(rm -> rm
                .key(rememberMeKey)
                .tokenValiditySeconds(7 * 24 * 60 * 60)
                .userDetailsService(storeUserDetailsService)
            )
            .logout(logout -> logout
                .logoutUrl("/store/logout")
                .logoutSuccessUrl("/store/login?logout")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    @Order(4)
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        var manager = new InMemoryUserDetailsManager(
            User.builder().username(adminUsername).password(passwordEncoder().encode(adminPassword)).roles("ADMIN").build(),
            User.builder().username("aj").password(passwordEncoder().encode(ajPassword)).roles("ADMIN").build(),
            User.builder().username("bill").password(passwordEncoder().encode(billPassword)).roles("ADMIN").build(),
            User.builder().username("dom").password(passwordEncoder().encode(domPassword)).roles("ADMIN").build(),
            User.builder().username("seth").password(passwordEncoder().encode(sethPassword)).roles("ADMIN").build(),
            User.builder().username("trevor").password(passwordEncoder().encode(trevorPassword)).roles("ADMIN").build(),
            User.builder().username("jared").password(passwordEncoder().encode(jaredPassword)).roles("ADMIN").build()
        );
        http
            .userDetailsService(manager)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/Images/**").permitAll()
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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
