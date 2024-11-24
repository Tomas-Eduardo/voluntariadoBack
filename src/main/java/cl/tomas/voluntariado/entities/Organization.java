package cl.tomas.voluntariado.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "users", "events"})
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la organización es requerido")
    private String nombre;

    @NotBlank(message = "El tipo de organización es requerido")
    private String tipo;

    @NotBlank(message = "La descripción de la organización es requerida")
    @Size(min = 5, max = 255, message = "La descripción debe tener entre 5 y 255 caracteres")
    private String descripcion;

    @NotBlank(message = "La dirección de la organización es requerida")
    private String direccion;

    @NotBlank(message = "El teléfono de la organización es requerido")
    private String telefono;

    @Email
    @NotBlank(message = "El email de la organización es requerido")
    private String email;

    private String website;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<UserEntity> organizadores;

    @OneToMany(mappedBy = "organizacion", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Event> eventos;


}
