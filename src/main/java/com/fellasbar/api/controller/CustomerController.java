package com.fellasbar.api.controller;

import com.fellasbar.api.model.Customer;
import com.fellasbar.api.model.PasswordResetToken;
import com.fellasbar.api.repository.CustomerRepository;
import com.fellasbar.api.repository.OrderRepository;
import com.fellasbar.api.repository.PasswordResetTokenRepository;
import com.fellasbar.api.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Controller
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;

    public CustomerController(CustomerRepository customerRepository,
                               OrderRepository orderRepository,
                               PasswordEncoder passwordEncoder,
                               PasswordResetTokenRepository resetTokenRepository,
                               EmailService emailService) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
        this.resetTokenRepository = resetTokenRepository;
        this.emailService = emailService;
    }

    @GetMapping("/store/orders")
    public String orders(Authentication auth, Model model) {
        model.addAttribute("orders", orderRepository.findAllByCustomerEmailOrderByCreatedAtDesc(auth.getName()));
        return "store-orders";
    }

    @GetMapping("/store/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "Invalid email or password.");
        if (logout != null) model.addAttribute("message", "You've been signed out.");
        return "store-login";
    }

    @GetMapping("/store/register")
    public String registerPage() {
        return "store-register";
    }

    @PostMapping("/store/register")
    public String register(@RequestParam String email,
                           @RequestParam String phone,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           Model model) {
        email = email.trim().toLowerCase();

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "store-register";
        }

        var existing = customerRepository.findByEmail(email);
        if (existing.isPresent()) {
            if (existing.get().hasPassword()) {
                model.addAttribute("error", "An account with that email already exists.");
                return "store-register";
            }
            // Guest record from a previous order — just set the password
            Customer customer = existing.get();
            if (customer.getPhone() == null) customer.setPhone(phone.trim());
            customer.setPasswordHash(passwordEncoder.encode(password));
            customerRepository.save(customer);
            return "redirect:/store/login?registered";
        }

        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPhone(phone.trim());
        customer.setPasswordHash(passwordEncoder.encode(password));
        customerRepository.save(customer);

        return "redirect:/store/login?registered";
    }

    @GetMapping("/store/forgot-password")
    public String forgotPasswordPage() {
        return "store-forgot-password";
    }

    @PostMapping("/store/forgot-password")
    public String forgotPasswordSubmit(@RequestParam String email, Model model) {
        final String normalizedEmail = email.trim().toLowerCase();
        customerRepository.findByEmail(normalizedEmail).ifPresent(customer -> {
            resetTokenRepository.deleteByEmail(normalizedEmail);
            PasswordResetToken prt = new PasswordResetToken();
            prt.setToken(UUID.randomUUID().toString());
            prt.setEmail(normalizedEmail);
            prt.setExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS));
            resetTokenRepository.save(prt);
            emailService.sendPasswordReset(normalizedEmail, baseUrl + "/store/reset-password?token=" + prt.getToken());
        });
        // Always show the same message to avoid revealing whether the email exists
        model.addAttribute("sent", true);
        return "store-forgot-password";
    }

    @GetMapping("/store/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        var prt = resetTokenRepository.findByToken(token);
        if (prt.isEmpty() || prt.get().isExpired()) {
            model.addAttribute("error", "This reset link is invalid or has expired.");
            return "store-forgot-password";
        }
        model.addAttribute("token", token);
        return "store-reset-password";
    }

    @PostMapping("/store/reset-password")
    public String resetPasswordSubmit(@RequestParam String token,
                                       @RequestParam String password,
                                       @RequestParam String confirmPassword,
                                       Model model) {
        var prt = resetTokenRepository.findByToken(token);
        if (prt.isEmpty() || prt.get().isExpired()) {
            model.addAttribute("error", "This reset link is invalid or has expired.");
            return "store-forgot-password";
        }
        if (!password.equals(confirmPassword)) {
            model.addAttribute("token", token);
            model.addAttribute("error", "Passwords do not match.");
            return "store-reset-password";
        }
        if (password.length() < 8) {
            model.addAttribute("token", token);
            model.addAttribute("error", "Password must be at least 8 characters.");
            return "store-reset-password";
        }
        String email = prt.get().getEmail();
        Customer customer = customerRepository.findByEmail(email).orElseGet(() -> {
            Customer c = new Customer();
            c.setEmail(email);
            return c;
        });
        customer.setPasswordHash(passwordEncoder.encode(password));
        customerRepository.save(customer);
        resetTokenRepository.delete(prt.get());
        return "redirect:/store/login?reset";
    }
}
