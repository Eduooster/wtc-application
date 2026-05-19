package org.wtc.application.user.service;

import org.wtc.application.user.dto.UpdateUserSegmentsRequestDTO;
import org.wtc.application.user.dto.UserRequestDTO;
import org.wtc.application.user.dto.UserResponseDTO;

import java.util.List;
import java.util.Set;

public interface IUserService {
    UserResponseDTO createUser(UserRequestDTO userRequest);
    UserResponseDTO findById(Long id);
    List<UserResponseDTO> findAll();
    UserResponseDTO updateUser(Long id, UserRequestDTO userRequest);
    void deleteUser(Long id);


}
