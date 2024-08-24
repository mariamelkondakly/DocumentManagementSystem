package com.AtosReady.UserManagementSystem.Services;


import com.AtosReady.UserManagementSystem.Models.User;
import com.AtosReady.UserManagementSystem.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServices implements UserDetailsService {

    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=repo.findByEmail(email);

        if(user== null)
        {
            throw new UsernameNotFoundException("User Not Found");
        }
        return user;
    }
}
