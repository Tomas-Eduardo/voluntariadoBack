package cl.tomas.voluntariado.entities;

import cl.tomas.voluntariado.models.IUserEntity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements IUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email()
    @Column(unique = true)
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotNull(message = "La password es obligatoria")
    private String password;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean organizador;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "users"})
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"), uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "role_id"})})
    private List<Role> roles;

    // Relación con organización (solo para organizadores)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "users"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    // Relación con eventos (solo para voluntarios)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "voluntarios"})
    @ManyToMany(mappedBy = "voluntarios", fetch = FetchType.LAZY)
    private List<Event> eventos;

    @JsonCreator
    public UserEntity(
            @JsonProperty("id") Long id,
            @JsonProperty("nombre") String nombre,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("fechaNacimiento") LocalDate fechaNacimiento
    ) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.fechaNacimiento = fechaNacimiento;
    }



}
