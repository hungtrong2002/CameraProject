package com.camera.map;

import org.springframework.stereotype.Component;

import com.camera.dto.UserDTO;
import com.camera.entity.UserEntity;
import com.camera.security.service.UserDetailsImpl;

@Component
public class UserMapper {

    public UserDTO toDto(UserDetailsImpl user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getUsername());
        dto.setUserName(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
