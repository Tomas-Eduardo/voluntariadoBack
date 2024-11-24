package cl.tomas.voluntariado.controllers;

import cl.tomas.voluntariado.dto.OrganizationDTO;
import cl.tomas.voluntariado.entities.Organization;
import cl.tomas.voluntariado.services.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations() {
        List<Organization> organizations = organizationService.findAll();
        List<OrganizationDTO> dtoList = organizations.stream()
                .map(org -> new OrganizationDTO(org))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Organization organization, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        Organization savedOrganization = organizationService.save(organization);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrganization);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<Organization> organization = organizationService.findById(id);
        return organization.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body((Organization) Collections.singletonMap("error", "Organization not found")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Organization organization, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validation(result);
        }

        Optional<Organization> existingOrg = organizationService.findById(id);
        if (existingOrg.isPresent()) {
            Organization updatedOrg = existingOrg.get();
            updatedOrg.setEmail(organization.getEmail());
            updatedOrg.setTelefono(organization.getTelefono());
            updatedOrg.setDireccion(organization.getDireccion());
            updatedOrg.setDescripcion(organization.getDescripcion());
            updatedOrg.setNombre(organization.getNombre());
            updatedOrg.setTipo(organization.getTipo());
            updatedOrg.setWebsite(organization.getWebsite());
            return ResponseEntity.status(HttpStatus.OK).body(organizationService.save(updatedOrg));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Organization not found"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Organization> organization = organizationService.findById(id);
        if (organization.isPresent()) {
            organizationService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Organization not found"));
        }
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    // MÉTODOS ADICIONALES SEGÚN AUTENTICACIÓN
    @GetMapping("/search-org")
    public ResponseEntity<Organization> obtenerOrganizacionDelOrganizador(Authentication authentication) {
        String email = authentication.getName();
        Organization organizacion = organizationService.obtenerOrganizacionPorEmail(email);
        return ResponseEntity.ok(organizacion);
    }

    @GetMapping("/user")
    public ResponseEntity<Organization> getOrganizationByUserId(Authentication authentication) {
        String username = authentication.getName();
        Organization organization = organizationService.findOrganizationByUser(username);

        if (organization != null) {
            return ResponseEntity.ok(organization);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
