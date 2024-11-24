package cl.tomas.voluntariado.services;

import cl.tomas.voluntariado.dto.OrganizationDTO;
import cl.tomas.voluntariado.entities.Organization;

import java.util.List;
import java.util.Optional;

public interface OrganizationService {

    List<Organization> findAll();
    Optional<Organization> findById(Long id);
    Organization save(Organization organization);
    void deleteById(Long id);

    Organization obtenerOrganizacionPorEmail(String email);

    Organization findOrganizationByUser(String username);

    Optional<OrganizationDTO> getOrganizationById(Long id);
}
