package com.example.usermanagement.mapper;

import com.example.usermanagement.dto.UserDto;
import com.example.usermanagement.repository.bean.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;


@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public abstract class UserMapper {

    public abstract User userDtoToUser(UserDto userDto);
}
