package cl.tomas.voluntariado.services;

import cl.tomas.voluntariado.entities.Role;
import cl.tomas.voluntariado.entities.UserEntity;
import cl.tomas.voluntariado.models.IUserEntity;
import cl.tomas.voluntariado.models.UserEntityRequest;
import cl.tomas.voluntariado.repositories.RoleRepository;
import cl.tomas.voluntariado.repositories.UserEntityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserEntityServiceImpl implements UserEntityService {

    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userEntityRepository;
    private final RoleRepository roleRepository;

    public UserEntityServiceImpl(UserEntityRepository userEntityRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<UserEntity> findAll() {
        return (List<UserEntity>) userEntityRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UserEntity> findById(Long id) {
        return userEntityRepository.findById(id);
    }

    @Override
    public UserEntity save(UserEntity user) {
        user.setRoles(getRoles(user));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userEntityRepository.save(user);
    }


    @Override
    public Optional<UserEntity> update(UserEntityRequest user, Long id) {
        Optional<UserEntity> userEntityOptional = userEntityRepository.findById(id);

        if (userEntityOptional.isPresent()) {
            UserEntity userDb = userEntityOptional.get();
            userDb.setEmail(user.getEmail());
            userDb.setNombre(user.getNombre());

            userDb.setRoles(getRoles(user));
            return Optional.of(userEntityRepository.save(userDb));
        }
        return Optional.empty();
    }

    private List<Role> getRoles(IUserEntity user) {
        List<Role> roles = new ArrayList<>();
        Optional<Role> optionalRoleUser = roleRepository.findByName("ROLE_USER");
        optionalRoleUser.ifPresent(roles::add);

        if (user.isAdmin()) {
            Optional<Role> optionalRoleAdmin = roleRepository.findByName("ROLE_ADMIN");
            optionalRoleAdmin.ifPresent(roles::add);
        } else if(user.isOrganizador()) {
            Optional<Role> optionalRoleOrganizador = roleRepository.findByName("ROLE_ORGANIZADOR");
            optionalRoleOrganizador.ifPresent(roles::add);
        }
        return roles;
    }

    @Override
    public void deleteById(Long id) {
        userEntityRepository.deleteById(id);
    }

}
