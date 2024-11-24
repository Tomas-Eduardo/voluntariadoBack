package cl.tomas.voluntariado.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del evento es obligatorio")
    private String nombre;

    @NotNull(message = "La fecha del evento es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El horario del evento es obligatorio")
    private String horario;

    @NotBlank(message = "La descripción del evento es obligatoria")
    private String descripcion;

    @NotBlank(message = "La dirección del evento es obligatoria")
    private String direccion;

    @NotBlank(message = "La ciudad del evento es obligatoria")
    private String ciudad;

    @NotBlank(message = "El estado del evento es obligatorio")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "organizacion_id", nullable = false)
    private Organization organizacion;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_volunteers",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "volunteer_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"event_id", "volunteer_id"})})
    private List<UserEntity> voluntarios;

    public void addVoluntario(UserEntity user) {
        voluntarios.add(user);
    }
}
