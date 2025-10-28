package com.hmanh.ecommerce.service.impl;

import com.hmanh.ecommerce.Entity.User;
import com.hmanh.ecommerce.repository.UserRepository;
import com.hmanh.ecommerce.service.AuthService;
import com.hmanh.ecommerce.service.UserService;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SINGER_KEY;

    @Override
    public User findUserByJwtToken(String jwtToken) throws Exception {
        if (jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
        }
        JWSVerifier jwsVerifier = new MACVerifier(SINGER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(jwtToken);
        boolean verified = signedJWT.verify(jwsVerifier);
        if (!verified) {
            throw new Exception("JWT signature verification failed");
        }
        Date expTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expTime.before(new Date())) {
            throw new Exception("JWT token has expired");
        }
        String email = signedJWT.getJWTClaimsSet().getSubject();
        if (email == null) {
            throw new Exception("JWT token does not contain subject");
        }
        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("user not found");
        }
        return user;
    }
}
