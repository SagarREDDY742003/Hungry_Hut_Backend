package com.sagar.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtTokenValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // This is the core method that gets called for each HTTP request.

        String jwt = request.getHeader(JwtConstant.JWT_HEADER);
        // Retrieves the JWT from the request header (e.g., "Authorization").
        //JwtConstant.JWT_HEADER is a constant that holds the header name.

        if(jwt != null){
            jwt = jwt.substring(7);
            // JWTs are usually prefixed with "Bearer " in headers. This removes that prefix to get the raw token.

            try {
                // Uses the secret key to validate the JWT signature.
                // If valid, extracts the claims (payload data) from the token.
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();



                // Gets the user's email and roles/authorities from the token.
                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                // Converts the authorities string into a list of GrantedAuthority.
                List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                //Creates an Authentication object with the user's email and roles.
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auth);

                //Sets this authentication in the SecurityContext, so Spring Security knows the user is authenticated.
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                throw new BadCredentialsException("Invalid token......");
            }
        }
        // Passes the request along the filter chain, allowing other filters or controllers to process it.
        filterChain.doFilter(request,response);
    }
}
