package com.codewithmosh.store.auth;

import com.codewithmosh.store.users.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

import javax.crypto.SecretKey;
import java.util.Date;

@AllArgsConstructor
public class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;

    public Long getUserId() {
        return Long.valueOf(claims.getSubject());
    }

    public Role getRole() {
        return Role.valueOf(claims.get("role", String.class));
    }

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public String toString(){
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }
}
