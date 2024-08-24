package com.AtosReady.UserManagementSystem.Mappers;

import com.AtosReady.UserManagementSystem.DTO.UserDTO;
import com.AtosReady.UserManagementSystem.Models.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-23T23:14:40+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 22.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setEmail( user.getEmail() );
        userDTO.setNid( user.getNid() );
        userDTO.setFirst_name( user.getFirst_name() );
        userDTO.setLast_name( user.getLast_name() );
        userDTO.setAge( String.valueOf( user.getAge() ) );

        return userDTO;
    }

    @Override
    public User userDTOToUser(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( userDTO.getEmail() );
        user.setNid( userDTO.getNid() );
        user.setFirst_name( userDTO.getFirst_name() );
        user.setLast_name( userDTO.getLast_name() );
        if ( userDTO.getAge() != null ) {
            user.setAge( Integer.parseInt( userDTO.getAge() ) );
        }

        return user;
    }
}
