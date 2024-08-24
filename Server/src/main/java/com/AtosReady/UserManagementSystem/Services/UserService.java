package com.AtosReady.DocumentManagementSystem.Services;

import com.AtosReady.DocumentManagementSystem.DTO.UserDTO;
import com.AtosReady.DocumentManagementSystem.DTO.LoginRequest;
import com.AtosReady.DocumentManagementSystem.Mappers.UserMapper;
import com.AtosReady.DocumentManagementSystem.Models.User;
import com.AtosReady.DocumentManagementSystem.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo repo;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authManager;

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(12);
    }

    public UserDTO register(User user){
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        user.getRoles().add("ROLE_USER");
        repo.save(user);
        return userMapper.userToUserDTO(user);
    }

    public String verify(LoginRequest user){
        Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if(authentication.isAuthenticated()){
            return jwtService.generateToken(user.getEmail(), user.getId());
        }
        else{
            throw new UsernameNotFoundException("User doesn't exist.");
        }
    }

    public ArrayList<UserDTO> geAllUsers(){
        List<User>users=repo.findAll();
        ArrayList<UserDTO> abstractUsers=new ArrayList<>();
       for(User user:users) {
            abstractUsers.add(userMapper.userToUserDTO(user));
       }
        return abstractUsers;
    }
}
