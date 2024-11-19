package cl.tomas.voluntariado.services;

import cl.tomas.voluntariado.entities.UserEntity;
import cl.tomas.voluntariado.models.UserEntityRequest;

import java.util.List;
import java.util.Optional;

public interface UserEntityService {

    List<UserEntity> findAll();
    Optional<UserEntity> findById(Long id);
    UserEntity save(UserEntity user);
    Optional<UserEntity> update(UserEntityRequest user, Long id);
    void deleteById(Long id);

}
