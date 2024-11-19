package cl.tomas.voluntariado.controllers;

import cl.tomas.voluntariado.entities.Contact;
import cl.tomas.voluntariado.services.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public List<Contact> list(){
        return contactService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Contact contact, BindingResult result){
        if(result.hasErrors()){
            return validation(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(contactService.save(contact));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<Contact> contactOptional = contactService.findById(id);
        if (contactOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(contactOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Contact not found"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Contact> contactOptional = contactService.findById(id);
        if (contactOptional.isPresent()) {
            contactService.deleteById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Contact not found"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Contact contact, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validation(result);
        }

        Optional<Contact> contactOptional = contactService.findById(id);

        if (contactOptional.isPresent()) {
            Contact contactDb = contactOptional.get();
            contactDb.setEmail(contact.getEmail());
            contactDb.setNombre(contact.getNombre());
            contactDb.setMensaje(contact.getMensaje());
            return ResponseEntity.status(HttpStatus.CREATED).body(contactService.save(contactDb));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Contact not found"));
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
