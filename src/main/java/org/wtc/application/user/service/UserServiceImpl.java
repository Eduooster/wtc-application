package org.wtc.application.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.wtc.application.segment.entity.Segment;
import org.wtc.application.user.dto.UpdateUserSegmentsRequestDTO;
import org.wtc.application.user.dto.UserRequestDTO;
import org.wtc.application.user.dto.UserResponseDTO;
import org.springframework.stereotype.Service;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.mapper.UserMapper;
import org.wtc.application.user.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequest) {
        User user = userMapper.toEntity(userRequest);


        user.setActive(true);
        user.setDeleted(false);

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO findById(Long id) {
        return userRepository.findByIdAndDeletedFalse(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return userRepository.findAllByDeletedFalse().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequest) {
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        userMapper.updateEntityFromDto(userRequest, user);

//        if (userRequest.getPassword() != null && !userRequest.getPassword().isBlank()) {
//            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
//        }

        return userMapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        user.setDeleted(true);
        user.setActive(false);
        userRepository.save(user);
    }


}