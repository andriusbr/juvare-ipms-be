package com.juvare.ipms.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.juvare.ipms.contract.AuthenticationRequest;
import com.juvare.ipms.contract.AuthenticationResponse;
import com.juvare.ipms.exception.ResourceNotFoundException;
import com.juvare.ipms.model.User;
import com.juvare.ipms.model.UserAuth;
import com.juvare.ipms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Supplier;

@Service
public class AuthenticationService {

    private final int expirationInMinutes;
    private final JWTVerifier verifier;
    private final Algorithm algorithm;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationService(
        @Value("${auth.secret}") String secret,
        @Value("${auth.jwtExpirationInMinutes:60}") int expirationInMinutes,
        UserRepository userRepository,
        PasswordEncoder passwordEncoder) {

        this.expirationInMinutes = expirationInMinutes;
        this.algorithm = Algorithm.HMAC256(secret);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.verifier = JWT.require(algorithm).withIssuer("IPMS").build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        tryAuthenticate(request);

        var accessToken = JWT.create()
            .withClaim("username", request.getUsername())
            .withIssuer("IPMS")
            .withExpiresAt(getExpiration())
            .sign(algorithm);

        return new AuthenticationResponse(accessToken, expirationInMinutes);
    }

    public DecodedJWT decodeToken(String token) {
        return verifier.verify(token);
    }

    public UserAuth loadUserDetails(String username) {
        return userRepository.findByUsername(username)
            .map(user -> new UserAuth(user.getUsername(), "", new ArrayList<SimpleGrantedAuthority>(), user.getId()))
            .orElseThrow(() -> new ResourceNotFoundException("User by username not found=" + username));
    }

    private void tryAuthenticate(AuthenticationRequest request) {
        var userEntity = userRepository.findByUsername(request.getUsername())
            .orElseThrow(supplyAuthorizationException());

        if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            throw supplyAuthorizationException().get();
        }
    }

    private Supplier<AuthorizationServiceException> supplyAuthorizationException() {
        return () -> new AuthorizationServiceException("Incorrect credentials");
    }

    private Date getExpiration() {
        var expirationDate = LocalDateTime.now().plus(expirationInMinutes, ChronoUnit.MINUTES);

        return Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant());
    }
}
