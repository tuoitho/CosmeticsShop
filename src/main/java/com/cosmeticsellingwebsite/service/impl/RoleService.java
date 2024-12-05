package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.entity.Role;
import com.cosmeticsellingwebsite.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
