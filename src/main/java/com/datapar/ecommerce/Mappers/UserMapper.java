package com.datapar.ecommerce.Mappers;

import com.datapar.ecommerce.User.RegisterDTO;
import com.datapar.ecommerce.User.User;
import com.datapar.ecommerce.User.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO userToUserDTO(User user);

    @Mapping(target = "password", ignore = true)
    User registerDTOToUser(RegisterDTO registerDTO);
}
