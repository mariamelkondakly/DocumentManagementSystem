package com.AtosReady.UserManagementSystem.Controllers;

import com.AtosReady.UserManagementSystem.DTO.LoginRequest;
import com.AtosReady.UserManagementSystem.DTO.UserDTO;
import com.AtosReady.UserManagementSystem.Mappers.UserMapper;
import com.AtosReady.UserManagementSystem.Models.User;
import com.AtosReady.UserManagementSystem.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public UserDTO register( @Valid @RequestBody User user)
    {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody  LoginRequest user)
    {
        return userService.verify(user);
    }



    @GetMapping("/Users")
    public ArrayList<UserDTO> getUsers() {
        return userService.geAllUsers();
    }

}
