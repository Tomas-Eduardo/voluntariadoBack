package cl.tomas.voluntariado.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventDTO {

    private Long id;
    private String nombre;
    private LocalDate fecha;
    private String horario;
    private String descripcion;
    private String direccion;
    private String ciudad;
    private String estado;
    private Long organizationId;

    private List<Long> voluntarios; // Lista de IDs de voluntarios
    @Setter
    private boolean isUserRegistered; // Nuevo campo

    public void setIsUserRegistered(boolean isUserRegistered) {
        this.isUserRegistered = isUserRegistered;
    }
}
