package cl.tomas.voluntariado.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
