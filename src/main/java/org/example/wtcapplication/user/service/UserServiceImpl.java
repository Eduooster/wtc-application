package org.example.wtcapplication.user.service;

import org.example.wtcapplication.user.dto.UserRequestDTO;
import org.example.wtcapplication.user.dto.UserResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService{
    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequest) {
        return null;
    }

    @Override
    public UserResponseDTO findById(Long id) {
        return null;
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return List.of();
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequest) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
