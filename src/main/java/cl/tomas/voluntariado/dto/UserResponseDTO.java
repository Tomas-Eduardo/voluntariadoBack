package cl.tomas.voluntariado.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String nombre;
    private String email;
    private List<String> roles; // Solo los nombres de los roles
    private String organizationName; // Nombre de la organización (si existe)
}
