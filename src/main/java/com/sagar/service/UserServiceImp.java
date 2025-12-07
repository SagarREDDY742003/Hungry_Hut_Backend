package com.sagar.service;

import com.sagar.config.JwtProvider;
import com.sagar.model.Users;
import com.sagar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp  implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public Users findUserByJwtToken(String jwt) throws Exception {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        Users user = findUserByEmail(email);

        return user;
    }

    @Override
    public Users findUserByEmail(String email) throws Exception {
        Users user = userRepository.findByEmail(email);

        if(user==null)
            throw new Exception("user not found");

        return user;
    }
}
