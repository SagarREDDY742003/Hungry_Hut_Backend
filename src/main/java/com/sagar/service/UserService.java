package com.sagar.service;

import com.sagar.model.Users;
import org.springframework.context.annotation.Bean;

public interface UserService {

    public Users findUserByJwtToken(String jwt) throws Exception;

    public Users findUserByEmail(String email) throws Exception;

}
