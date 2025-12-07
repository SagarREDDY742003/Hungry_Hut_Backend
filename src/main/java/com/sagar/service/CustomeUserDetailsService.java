package com.sagar.service;

import com.sagar.model.USER_ROLE;
import com.sagar.model.Users;
import com.sagar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// CustomeUserDetailsService is a custom implementation of Spring Security's UserDetailsService interface.
// It's used to load user-specific data during authentication.
@Service

public class CustomeUserDetailsService implements UserDetailsService {

    // Injects UserRepository, which is presumably a Spring Data JPA repository used to fetch User entities from the database.
    @Autowired
    private UserRepository userRepository;

    // This method is called by Spring Security during login to retrieve user details using the provided username (in this case, email).
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{

        // Fetches the user from the database using their email
        Users user = userRepository.findByEmail(username);

        // If no user is found, throws an exception that Spring Security will handle (e.g., by rejecting login).
        if(user==null){
            throw new UsernameNotFoundException("user not found with email" + username);
        }

        // Retrieves the user's role (likely an enum USER_ROLE).
        USER_ROLE role = user.getRole();

        // Converts the role into a GrantedAuthority, which Spring Security uses to manage access control.
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(role.toString()));

        /*
            Returns a Spring Security User object containing:
            Username (email)
            Password (hashed)
            Authorities (roles)
        */
        return  new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

    }
}
