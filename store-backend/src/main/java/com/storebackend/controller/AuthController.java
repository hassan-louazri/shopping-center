package com.storebackend.controller;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Profile("dev")
public class AuthController {
    private JwtEncoder jwtEncoder;

    private AuthenticationManager authenticationManager;


    public AuthController(JwtEncoder jwtEncoder, AuthenticationManager authenticationManager) {
        this.jwtEncoder = jwtEncoder;
        this.authenticationManager = authenticationManager;

    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> jwtToken(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        String subject = authentication.getName();
        String scope = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Instant instant = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(instant)
                .expiresAt(instant.plus(5, ChronoUnit.MINUTES))
                .issuer("Store_Hsina")
                .claim("scope", scope)
                .build();

        String jwtAccessToken = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet)).getTokenValue();

        Map<String, String> idToken = new HashMap<>();
        idToken.put("accessToken", jwtAccessToken);

        return new ResponseEntity<>(idToken, HttpStatus.OK);
    }

}