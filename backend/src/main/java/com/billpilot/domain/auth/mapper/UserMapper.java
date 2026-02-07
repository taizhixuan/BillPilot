package com.billpilot.domain.auth.mapper;

import com.billpilot.domain.auth.dto.UserResponse;
import com.billpilot.domain.auth.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
    List<UserResponse> toResponseList(List<User> users);
}
