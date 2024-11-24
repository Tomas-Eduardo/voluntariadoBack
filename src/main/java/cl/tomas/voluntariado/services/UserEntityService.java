package cl.tomas.voluntariado.services;

import cl.tomas.voluntariado.entities.UserEntity;
import cl.tomas.voluntariado.models.UserEntityRequest;
import org.apache.catalina.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserEntityService {

    List<UserEntity> findAll();
    Optional<UserEntity> findById(Long id);
    UserEntity save(UserEntity user);
    Optional<UserEntity> update(UserEntityRequest user, Long id);
    void deleteById(Long id);
    Optional<UserEntity>convertToOrganizer(Long userId, Long organizationId);
    Optional<UserEntity> findByEmail(String email);

}
