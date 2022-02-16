package com.stackroute.utility;

import com.stackroute.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {
    static final long EXPIRATIONTIME = 3600000L;
    static String SIGNINGKEY;
    static final String PREFIX = "Bearer";

    public JwtUtil() {
    }

    @Value("${SIGNING_KEY}")
    public void setSigningkey(String signingkey) {
        SIGNINGKEY = signingkey;
    }

    public static String addUserToken(HttpServletResponse res, Optional<User> user) {
        Claims claims = Jwts.claims();
        System.out.println("SIGNINGKEY" + SIGNINGKEY);
        User user1 = user.get();
        claims.put("userId", user1.getUserId());
        claims.put("username", user1.getUsername());
        claims.put("firstName", user1.getFirstName());
        claims.put("lastName", user1.getLastName());
        claims.put("avatar", user1.getAvatarUrl());
        claims.put("email", user1.getEmail());
        String jwtToken = Jwts.builder().setClaims(claims).setExpiration(new Date(System.currentTimeMillis() + 3600000L)).signWith(SignatureAlgorithm.HS512, SIGNINGKEY).compact();
        return jwtToken;
    }

}

