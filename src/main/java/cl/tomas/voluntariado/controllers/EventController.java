package cl.tomas.voluntariado.controllers;

import cl.tomas.voluntariado.dto.EventDTO;
import cl.tomas.voluntariado.entities.Event;
import cl.tomas.voluntariado.entities.UserEntity;
import cl.tomas.voluntariado.exceptions.ResourceNotFoundException;
import cl.tomas.voluntariado.services.EventService;
import cl.tomas.voluntariado.services.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserEntityService userEntityService;

    @GetMapping
    public ResponseEntity<List<Event>> listAll() {
        List<Event> events = eventService.findAll();
        return ResponseEntity.ok(events);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Event event, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Event savedEvent = eventService.save(event);
        return ResponseEntity.status(201).body(savedEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result); // Si hay errores de validación, retorna los errores
        }

        Optional<Event> existingEvent = eventService.findById(id);
        if (existingEvent.isPresent()) {
            // Si el evento existe, actualiza los datos
            Event eventToUpdate = existingEvent.get();

            // Actualizamos la entidad Event con los valores de EventDTO
            eventToUpdate.setNombre(eventDTO.getNombre());
            eventToUpdate.setFecha(eventDTO.getFecha());
            eventToUpdate.setHorario(eventDTO.getHorario());
            eventToUpdate.setDescripcion(eventDTO.getDescripcion());
            eventToUpdate.setDireccion(eventDTO.getDireccion());
            eventToUpdate.setCiudad(eventDTO.getCiudad());
            eventToUpdate.setEstado(eventDTO.getEstado());

            // Guarda el evento actualizado
            eventService.save(eventToUpdate);
            return ResponseEntity.ok(eventToUpdate);  // Retorna el evento actualizado
        } else {
            // Si no se encuentra el evento con ese id, retorna un error 404
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Event not found"));
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<Event> event = eventService.findById(id);
        if (event.isPresent()) {
            return ResponseEntity.ok(event.get());
        } else {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Event not found"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        Optional<Event> event = eventService.findById(id);

        if (event.isPresent()) {
            // Eliminar el evento
            eventService.deleteById(id);
            return ResponseEntity.ok(Collections.singletonMap("message", "Evento eliminado"));
        }

        return ResponseEntity.status(404).body(Collections.singletonMap("error", "Evento no encontrado"));
    }




    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @PostMapping("/{eventId}/volunteers/{userId}")
    public ResponseEntity<Void> registerVolunteer(
            @PathVariable("eventId") Long eventId,
            @PathVariable("userId") Long userId) {

        // Obtener el evento y usuario usando Optional
        Optional<Event> eventOpt = eventService.findById(eventId);
        Optional<UserEntity> userOpt = userEntityService.findById(userId);

        // Verificar si el evento o el usuario no existen
        if (eventOpt.isEmpty() || userOpt.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Asegúrate de que el evento y el usuario existan.
        }

        // Obtener el objeto Event y UserEntity de los Optional
        Event event = eventOpt.get();
        UserEntity user = userOpt.get();

        // Verificar si el usuario ya está registrado en el evento
        if (event.getVoluntarios().stream().anyMatch(volunteer -> volunteer.getId().equals(userId))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // El usuario ya está registrado.
        }

        // Agregar el voluntario al evento
        event.addVoluntario(user);  // Este método debe estar en la clase Event, debes asegurarte de que exista.

        // Guardar el evento con el nuevo voluntario
        eventService.save(event);

        return ResponseEntity.ok().build();  // Respuesta exitosa.
    }




    @GetMapping("/available/{userId}")
    public ResponseEntity<List<EventDTO>> getAvailableEvents(@PathVariable("userId") Long userId) {
        // Obtener todos los eventos
        List<Event> allEvents = eventService.findAll();

        // Filtrar solo los eventos activos y en los que el usuario no está registrado
        List<EventDTO> availableEvents = allEvents.stream()
                .filter(event -> "Activo".equals(event.getEstado()) &&
                        event.getVoluntarios().stream().noneMatch(volunteer -> volunteer.getId().equals(userId)))
                .map(event -> {
                    EventDTO dto = new EventDTO();
                    dto.setId(event.getId());
                    dto.setNombre(event.getNombre());
                    dto.setFecha(event.getFecha());
                    dto.setHorario(event.getHorario());
                    dto.setDescripcion(event.getDescripcion());
                    dto.setCiudad(event.getCiudad());
                    dto.setDireccion(event.getDireccion());
                    dto.setEstado(event.getEstado());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(availableEvents);
    }

    @DeleteMapping("/{eventId}/volunteers/{volunteerId}")
    public ResponseEntity<Map<String, String>> removeVolunteerFromEvent(
            @PathVariable Long eventId, @PathVariable Long volunteerId) {

        Optional<Event> eventOpt = eventService.findById(eventId);
        Optional<UserEntity> volunteerOpt = userEntityService.findById(volunteerId);

        if (eventOpt.isPresent() && volunteerOpt.isPresent()) {
            Event event = eventOpt.get();
            UserEntity volunteer = volunteerOpt.get();

            // Comprobar si el voluntario está registrado en el evento
            if (event.getVoluntarios().contains(volunteer)) {
                event.getVoluntarios().remove(volunteer);  // Eliminar al voluntario
                eventService.save(event);  // Guardar los cambios en el evento
                return ResponseEntity.noContent().build();  // OK
            } else {
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "Voluntario no registrado en este evento"));
            }
        } else {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Evento o voluntario no encontrado"));
        }
    }

    @GetMapping("/registered/{userId}")
    public ResponseEntity<List<EventDTO>> getEventsByUser(@PathVariable("userId") Long userId) {
        // Encuentra todos los eventos donde el usuario esté registrado
        Optional<UserEntity> userOpt = userEntityService.findById(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }

        UserEntity user = userOpt.get();
        List<EventDTO> registeredEvents = user.getEventos().stream()
                .map(event -> {
                    EventDTO dto = new EventDTO();
                    dto.setId(event.getId());
                    dto.setNombre(event.getNombre());
                    dto.setFecha(event.getFecha());
                    dto.setHorario(event.getHorario());
                    dto.setDescripcion(event.getDescripcion());
                    dto.setCiudad(event.getCiudad());
                    dto.setDireccion(event.getDireccion());
                    dto.setEstado(event.getEstado());
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(registeredEvents);
    }



}
