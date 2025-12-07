package com.sagar.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

// JwtProvider is a service class that creates JWT tokens containing user information and roles.
// These tokens are typically used for stateless authentication in web applications.

@Service
public class JwtProvider {

    // Uses a secret key from JwtConstant.SECRET_KEY to create an HMAC SHA key.
    // This key is used to sign the JWT, ensuring its integrity and authenticity.
    private SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    // Takes an Authentication object (from Spring Security) as input.
    // This object contains the authenticated user's details, including their username and roles.
    public String generateToken(Authentication auth){

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populatedAuthorities(authorities);

        // Extracts the user's roles (authorities) and converts them into a comma-separated string.
        /*
            Builds the JWT:
                setIssuedAt: current time.
                setExpiration: 24 hours from now.
                claim("email", ...): stores the user's email (or username).
                claim("authorities", ...): stores the user's roles.
                signWith(key): signs the token with the secret key.
                compact(): finalizes the token as a string.
        */

        String jwt = Jwts.builder().setIssuedAt(new Date())
                .setExpiration((new Date(new Date().getTime()+86400000)))
                .claim("email",auth.getName())
                .claim("authorities",roles)
                .signWith(key)
                .compact();

        return jwt;
    }

    public String getEmailFromJwtToken(String jwt){
        jwt = jwt.substring(7);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));
        return email;
    }

    /*
        Converts a collection of GrantedAuthority objects into a comma-separated string of role names.
        Example: [ROLE_USER, ROLE_ADMIN] → "ROLE_USER,ROLE_ADMIN"
    */
    private String populatedAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();

        for(GrantedAuthority authority:authorities){
            auths.add(authority.getAuthority());
        }

        return String.join(",",auths);
    }
}
