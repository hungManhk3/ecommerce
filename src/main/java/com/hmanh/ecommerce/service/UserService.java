package com.hmanh.ecommerce.service;

import com.hmanh.ecommerce.Entity.User;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface UserService {
    User findUserByJwtToken(String jwtToken) throws Exception;
    User findUserByEmail(String email) throws Exception;
}
