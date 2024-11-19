package cl.tomas.voluntariado.services;

import cl.tomas.voluntariado.entities.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService {

    List<Contact> findAll();
    Optional<Contact> findById(Long id);
    Contact save(Contact contact);
    void deleteById(Long id);
}
