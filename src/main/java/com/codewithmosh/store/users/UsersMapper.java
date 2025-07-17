package com.codewithmosh.store.users;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void update(UpdateUserRequest updateUserRequest,@MappingTarget User user);
}
