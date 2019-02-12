package com.retail.productservice.auth.controller;

import com.retail.productservice.auth.service.UserAuthenticationService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

/**
 * LoginController to handle authentication process
 */
@RestController
@RequestMapping("/auth")
@FieldDefaults(level = PRIVATE, makeFinal = true)
@AllArgsConstructor(access = PACKAGE)
public class LoginController {

    @NonNull
    UserAuthenticationService authentication;

    @PostMapping("/login")
    public String login(@RequestParam("username") final String username,
                        @RequestParam("password") final String password){

        return authentication.login(username, password)
                .orElseThrow(() -> new RuntimeException("invalid login and/or password"));

    }
}
