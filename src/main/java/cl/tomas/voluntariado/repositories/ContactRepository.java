package cl.tomas.voluntariado.repositories;

import cl.tomas.voluntariado.entities.Contact;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, Long > {
}
