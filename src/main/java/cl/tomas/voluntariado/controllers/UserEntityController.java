package cl.tomas.voluntariado.controllers;

import cl.tomas.voluntariado.entities.UserEntity;
import cl.tomas.voluntariado.models.UserEntityRequest;
import cl.tomas.voluntariado.services.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserEntityController {

    @Autowired
    private UserEntityService userEntityService;

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

    private ResponseEntity<?> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errors.put(error.getField(),error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }


}
