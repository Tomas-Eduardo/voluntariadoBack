package cl.tomas.voluntariado.services;

import cl.tomas.voluntariado.dto.EventDTO;
import cl.tomas.voluntariado.entities.Event;
import cl.tomas.voluntariado.entities.UserEntity;
import cl.tomas.voluntariado.exceptions.ResourceNotFoundException;
import cl.tomas.voluntariado.repositories.EventRepository;
import cl.tomas.voluntariado.repositories.UserEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserEntityRepository userEntityRepository;

    public EventServiceImpl(EventRepository eventRepository, UserEntityRepository userEntityRepository) {
        this.eventRepository = eventRepository;
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public List<Event> findAll() {
        return (List<Event>) eventRepository.findAll();
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Event addVolunteer(Long eventId, Long volunteerId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        UserEntity volunteer = userEntityRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Voluntario no encontrado"));

        event.getVoluntarios().add(volunteer);
        return eventRepository.save(event);
    }

    @Override
    public Event removeVolunteer(Long eventId, Long volunteerId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        UserEntity volunteer = userEntityRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Voluntario no encontrado"));

        event.getVoluntarios().remove(volunteer);
        return eventRepository.save(event);
    }

    @Override
    public void registerVolunteerToEvent(Long eventId, Long volunteerId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado"));
        UserEntity volunteer = userEntityRepository.findById(volunteerId)
                .orElseThrow(() -> new ResourceNotFoundException("Voluntario no encontrado"));

        // Verificar si el voluntario ya está inscrito en el evento
        if (event.getVoluntarios().contains(volunteer)) {
            throw new IllegalStateException("El voluntario ya está inscrito en este evento");
        }

        // Agregar el voluntario al evento
        event.getVoluntarios().add(volunteer);
        eventRepository.save(event);
    }

    public List<Event> getAvailableEvents(Long volunteerId) {
        UserEntity volunteer = userEntityRepository.findById(volunteerId)
                .orElseThrow(() -> new ResourceNotFoundException("Voluntario no encontrado"));

        // Obtener todos los eventos
        List<Event> allEvents = (List<Event>) eventRepository.findAll();

        // Filtrar los eventos a los que el usuario no está inscrito
        return allEvents.stream()
                .filter(event -> !event.getVoluntarios().contains(volunteer))
                .collect(Collectors.toList());
    }
}


