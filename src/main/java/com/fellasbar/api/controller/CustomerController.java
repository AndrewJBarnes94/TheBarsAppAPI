package com.fellasbar.api.controller;

import com.fellasbar.api.model.Customer;
import com.fellasbar.api.repository.CustomerRepository;
import com.fellasbar.api.repository.OrderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerController(CustomerRepository customerRepository,
                               OrderRepository orderRepository,
                               PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.passwordEncoder = passwordEncoder;
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

        if (customerRepository.existsByEmail(email)) {
            model.addAttribute("error", "An account with that email already exists.");
            return "store-register";
        }

        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPhone(phone.trim());
        customer.setPasswordHash(passwordEncoder.encode(password));
        customerRepository.save(customer);

        return "redirect:/store/login?registered";
    }
}
