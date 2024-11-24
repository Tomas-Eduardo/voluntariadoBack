package cl.tomas.voluntariado.services;

import cl.tomas.voluntariado.entities.Role;
import cl.tomas.voluntariado.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return (List<Role>) roleRepository.findAll();
    }
}
