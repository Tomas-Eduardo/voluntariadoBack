package cl.tomas.voluntariado.dto;

import cl.tomas.voluntariado.entities.Organization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDTO {
    private Long id;
    private String nombre;
    private String tipo;
    private String telefono;
    private String email;
    private List<String> organizadores;
    private String descripcion;
    private String website;
    private String direccion;
    private List<EventDTO> eventos;

    public OrganizationDTO(Organization org) {
        this.id = org.getId();
        this.nombre = org.getNombre();
        this.tipo = org.getTipo();
        this.telefono = org.getTelefono();
        this.email = org.getEmail();
        this.descripcion = org.getDescripcion();
        this.website = org.getWebsite();
        this.direccion = org.getDireccion();
    }
}
