package cl.tomas.voluntariado.services;

import cl.tomas.voluntariado.dto.EventDTO;
import cl.tomas.voluntariado.entities.Event;
import cl.tomas.voluntariado.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> findAll();
    Optional<Event> findById(Long id);
    Event save(Event event);
    void deleteById(Long id);
    Event addVolunteer(Long eventId, Long volunteerId);
    Event removeVolunteer(Long eventId, Long volunteerId);
    void registerVolunteerToEvent(Long eventId, Long volunteerId);
    List<Event> getAvailableEvents(Long volunteerId);

}
