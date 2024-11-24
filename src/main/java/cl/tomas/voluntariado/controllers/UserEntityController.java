package cl.tomas.voluntariado.controllers;

import cl.tomas.voluntariado.dto.UserResponseDTO;
import cl.tomas.voluntariado.dto.UserUpdateDTO;
import cl.tomas.voluntariado.entities.Organization;
import cl.tomas.voluntariado.entities.Role;
import cl.tomas.voluntariado.entities.UserEntity;
import cl.tomas.voluntariado.models.UserEntityRequest;
import cl.tomas.voluntariado.repositories.OrganizationRepository;
import cl.tomas.voluntariado.repositories.RoleRepository;
import cl.tomas.voluntariado.repositories.UserEntityRepository;
import cl.tomas.voluntariado.services.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserEntityController {

    @Autowired
    private UserEntityService userEntityService;
    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    @GetMapping
    public List<UserEntity> list() {
        return userEntityService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UserEntity user, BindingResult result) {
        if (result.hasErrors()) {
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntityService.save(user));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<UserEntity> userEntityOptional = userEntityService.findById(id);
        if (userEntityOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userEntityOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User not found"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update (@Valid @RequestBody UserEntityRequest userEntity, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validation(result);
        }

        Optional<UserEntity> userEntityOptional = userEntityService.update(userEntity, id);

        if (userEntityOptional.isPresent()) {
            return ResponseEntity.ok(userEntityOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User not found"));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<UserEntity> userEntityOptional = userEntityService.findById(id);
        if (userEntityOptional.isPresent()) {
            userEntityService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.singletonMap("message", "User deleted"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User not found"));
    }

    @PostMapping("/convert-to-organizer")
    public ResponseEntity<?> convertToOrganizer(@RequestBody UserUpdateDTO userUpdateDTO) {
        if (userUpdateDTO.getUserId() == null) {
            return ResponseEntity.badRequest().body("El ID del usuario no puede ser nulo");
        }

        Optional<UserEntity> userOpt = userEntityRepository.findById(userUpdateDTO.getUserId());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        UserEntity user = userOpt.get();

        // Actualizar roles
        List<Role> newRoles = (List<Role>) roleRepository.findAllById(userUpdateDTO.getRoles());
        user.setRoles(newRoles);

        // Actualizar organización, si corresponde
        if (userUpdateDTO.getOrganizationId() != null) {
            Optional<Organization> orgOpt = organizationRepository.findById(userUpdateDTO.getOrganizationId());
            if (orgOpt.isPresent()) {
                user.setOrganization(orgOpt.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Organización no encontrada");
            }
        } else {
            user.setOrganization(null);
        }

        userEntityRepository.save(user);

        // Convertir y devolver DTO
        return ResponseEntity.ok(convertToDTO(user));
    }



    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(),error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    private UserResponseDTO convertToDTO(UserEntity user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setNombre(user.getNombre());
        dto.setEmail(user.getEmail());
        dto.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        dto.setOrganizationName(user.getOrganization() != null ? user.getOrganization().getNombre() : null);
        return dto;
    }

    @GetMapping("/id")
    public ResponseEntity<?> getLoggedInUserId() {
        // Obtiene el email del usuario autenticado desde el contexto de seguridad
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Busca el usuario por email
        Optional<UserEntity> userEntityOptional = userEntityService.findByEmail(email);
        if (userEntityOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userEntityOptional.get().getId());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "User not found"));
        }
    }



}
