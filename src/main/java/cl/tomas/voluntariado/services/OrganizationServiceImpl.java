package cl.tomas.voluntariado.services;

import cl.tomas.voluntariado.entities.Organization;
import cl.tomas.voluntariado.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService{

    private OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public List<Organization> findAll() {
        return (List<Organization>) organizationRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Organization> findById(Long id) {
        return organizationRepository.findById(id);
    }

    @Override
    public Organization save(Organization organization) {
        if (organization.getWebsite() != null) {
            organization.setWebsite(organization.getWebsite().toLowerCase());
        } else {
            organization.setWebsite("No website");
        }
        return organizationRepository.save(organization);
    }

    @Override
    public void deleteById(Long id) {
        organizationRepository.deleteById(id);
    }
}
