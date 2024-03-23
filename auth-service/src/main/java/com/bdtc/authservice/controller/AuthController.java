package com.bdtc.authservice.controller;

import com.bdtc.authservice.dto.AuthDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping()
    public ResponseEntity<AuthDto> userAuthentication(@AuthenticationPrincipal(expression = "claims['name']") String username, @AuthenticationPrincipal(expression = "claims['preferred_username']") String network_user) {
        AuthDto authDto = new AuthDto(network_user, username);
        return ResponseEntity.ok(authDto);
    }
}
