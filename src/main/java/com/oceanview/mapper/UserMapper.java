package com.oceanview.mapper;

import com.oceanview.dto.UserDTO;
import com.oceanview.model.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserMapper {
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUserName(user.getUserName());
        dto.setPassword(user.getPassword());
        dto.setUserEmail(user.getUserEmail());
        dto.setUserRole(user.getUserRole());
        dto.setUserStatus(user.getUserStatus());
        dto.setCreated_at(user.getCreated_at());
        dto.setUpdated_at(user.getUpdated_at());
        return dto;
    }

    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setUserName(dto.getUserName());
        user.setPassword(dto.getPassword());
        user.setUserEmail(dto.getUserEmail());
        user.setUserRole(dto.getUserRole());
        user.setUserStatus(dto.getUserStatus());
        user.setCreated_at(dto.getCreated_at());
        user.setUpdated_at(dto.getUpdated_at());
        return user;
    }

    public List<UserDTO> toDTOList(List<User> users) {
        if (users == null) {
            return Collections.emptyList();
        }
        return users.stream().filter(Objects::nonNull).map(this::toDTO).collect(Collectors.toList());
    }

    public List<User> toEntityList(List<UserDTO> dtos) {
        if (dtos == null) {
            return Collections.emptyList();
        }
        return dtos.stream().filter(Objects::nonNull).map(this::toEntity).collect(Collectors.toList());
    }
}
