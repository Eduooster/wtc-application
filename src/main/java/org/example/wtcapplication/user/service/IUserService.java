package org.example.wtcapplication.user.service;

import org.example.wtcapplication.user.dto.UserRequestDTO;
import org.example.wtcapplication.user.dto.UserResponseDTO;

import java.util.List;

public interface IUserService {
    UserResponseDTO createUser(UserRequestDTO userRequest);
    UserResponseDTO findById(Long id);
    List<UserResponseDTO> findAll();
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequest);
    void deleteUser(Long id);
}
