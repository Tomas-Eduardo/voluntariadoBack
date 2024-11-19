package cl.tomas.voluntariado.repositories;

import cl.tomas.voluntariado.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {


    // find email
    UserEntity findByEmail(String email);


}
