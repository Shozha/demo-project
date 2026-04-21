package com.technokratos.agona.service.impl;

import com.technokratos.agona.entity.Role;
import com.technokratos.agona.exception.auth.RoleNotFoundException;
import com.technokratos.agona.repository.RoleRepository;
import com.technokratos.agona.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException(name)) ;
    }
}
