package com.stackroute.cookie;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
public class CookieConfig {

    private String signingKey = "secret";

    public String getUserIdFromCookie(Cookie cookie){
        Claims claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(cookie.getValue()).getBody();
        return (String) claims.get("userId");
    }
}
