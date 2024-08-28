package com.AtosReady.UserManagementSystem.Services;

import com.AtosReady.UserManagementSystem.Creators.UserFolderCreator;
import com.AtosReady.UserManagementSystem.DTO.UserDTO;
import com.AtosReady.UserManagementSystem.DTO.LoginRequest;
import com.AtosReady.UserManagementSystem.Mappers.UserMapper;
import com.AtosReady.UserManagementSystem.Models.User;
import com.AtosReady.UserManagementSystem.Repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private UserFolderCreator folderCreator;

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(12);
    }

    public ResponseEntity<HashMap<String, Object>> register(User user){
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        user.getRoles().add("ROLE_USER");
        repo.save(user);
        return folderCreator.createUserDirectory(Long.toString(user.getNid()),userMapper.userToUserDTO(user));
    }

    public String verify(LoginRequest user){
        Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        User existingUser=repo.findByEmail(user.getEmail());
        if(authentication.isAuthenticated()){

            return jwtService.generateToken(user.getEmail(), existingUser.getNid(), existingUser.getFirst_name());
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
