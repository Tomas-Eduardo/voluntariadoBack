package cl.tomas.voluntariado.services;

import cl.tomas.voluntariado.dto.OrganizationDTO;
import cl.tomas.voluntariado.entities.Organization;
import cl.tomas.voluntariado.entities.UserEntity;
import cl.tomas.voluntariado.mapper.OrganizationMapper;
import cl.tomas.voluntariado.repositories.OrganizationRepository;
import cl.tomas.voluntariado.repositories.UserEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService{

    private final UserEntityRepository userEntityRepository;
    private OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, UserEntityRepository userEntityRepository) {
        this.organizationRepository = organizationRepository;
        this.userEntityRepository = userEntityRepository;
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

    @Override
    public Organization obtenerOrganizacionPorEmail(String email) {
        UserEntity user = userEntityRepository.findByEmail(email);

        // Manejo de excepciones
        if (user == null) {
            throw new RuntimeException("El usuario no existe");
        }

        if (user.getOrganization() == null) {
            throw new RuntimeException("El usuario no está asociado a ninguna organización");
        }

        return user.getOrganization();
    }

    @Override
    public Organization findOrganizationByUser(String username) {
        UserEntity user = userEntityRepository.findByEmail(username);
        if (user != null && user.getOrganization() != null) {
            return organizationRepository.findById(user.getOrganization().getId())
                    .orElse(null);
        }
        return null;
    }

    @Override
    public Optional<OrganizationDTO> getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .map(OrganizationMapper::toOrganizationDTO);
    }
}
