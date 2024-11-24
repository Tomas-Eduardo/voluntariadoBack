package cl.tomas.voluntariado.repositories;

import cl.tomas.voluntariado.entities.Event;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, Long > {
}
