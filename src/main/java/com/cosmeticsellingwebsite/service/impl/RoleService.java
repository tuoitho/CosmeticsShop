package com.cosmeticsellingwebsite.service.impl;

import com.cosmeticsellingwebsite.entity.Role;
import com.cosmeticsellingwebsite.repository.RoleRepository;
import com.cosmeticsellingwebsite.service.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleService implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findByRoleNames(List<String> roleNames) {
        return roleRepository.findByRoleNameIn(roleNames);
    }
}
