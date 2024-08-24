package com.AtosReady.DocumentManagementSystem.Mappers;

import com.AtosReady.DocumentManagementSystem.DTO.UserDTO;
import com.AtosReady.DocumentManagementSystem.Models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);
}
