package cl.tomas.voluntariado.repositories;

import cl.tomas.voluntariado.entities.Organization;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRepository extends CrudRepository<Organization, Long > {
}
