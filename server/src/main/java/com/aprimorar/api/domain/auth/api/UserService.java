package com.aprimorar.api.domain.auth.api;

import com.aprimorar.api.domain.auth.api.dto.UserCreateRequestDTO;
import com.aprimorar.api.domain.auth.api.dto.UserResponseDTO;
import java.util.List;
import java.util.UUID;

public interface UserService {

    List<UserResponseDTO> findAll();

    UserResponseDTO create(UserCreateRequestDTO request);

    void delete(UUID id);

    void deleteByEmployeeId(UUID employeeId);

    UserResponseDTO findByEmployeeId(UUID employeeId);
}
