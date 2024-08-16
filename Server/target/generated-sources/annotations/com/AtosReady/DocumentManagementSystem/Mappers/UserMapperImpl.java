package com.AtosReady.DocumentManagementSystem.Mappers;

import com.AtosReady.DocumentManagementSystem.DTO.UserDTO;
import com.AtosReady.DocumentManagementSystem.Models.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-16T23:07:57+0300",
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
        userDTO.setPersonal_info( user.getPersonal_info() );

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
        user.setPersonal_info( userDTO.getPersonal_info() );

        return user;
    }
}
