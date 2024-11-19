package cl.tomas.voluntariado.controllers;

import cl.tomas.voluntariado.entities.Organization;
import cl.tomas.voluntariado.services.OrganizationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping
    public List<Organization> list(){
        return organizationService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Organization organization, BindingResult result){
        if(result.hasErrors()){
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.save(organization));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<Organization> organizationOptional = organizationService.findById(id);
        if (organizationOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(organizationOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Organization not found"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Organization> organizationOptional = organizationService.findById(id);
        if (organizationOptional.isPresent()) {
            organizationService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Organization not found"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Organization organization, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validation(result);
        }

        Optional<Organization> organizationOptional = organizationService.findById(id);

        if (organizationOptional.isPresent()) {
            Organization organization1 = organizationOptional.get();
            organization1.setEmail(organization.getEmail());
            organization1.setTelefono(organization.getTelefono());
            organization1.setDireccion(organization.getDireccion());
            organization1.setDescripcion(organization.getDescripcion());
            organization1.setNombre(organization.getNombre());
            organization1.setTipo(organization.getTipo());
            organization1.setWebsite(organization.getWebsite());
            return ResponseEntity.status(HttpStatus.CREATED).body(organizationService.save(organization1));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Organization not found"));
        }
    }

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(),error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
