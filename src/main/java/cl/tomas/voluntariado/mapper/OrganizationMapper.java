package cl.tomas.voluntariado.mapper;

import cl.tomas.voluntariado.dto.OrganizationDTO;
import cl.tomas.voluntariado.dto.EventDTO;
import cl.tomas.voluntariado.entities.Organization;
import cl.tomas.voluntariado.entities.Event;
import cl.tomas.voluntariado.entities.UserEntity;

import java.util.stream.Collectors;

public class OrganizationMapper {

    public static OrganizationDTO toOrganizationDTO(Organization org) {
        OrganizationDTO dto = new OrganizationDTO();

        dto.setId(org.getId());
        dto.setNombre(org.getNombre());
        dto.setTipo(org.getTipo());
        dto.setTelefono(org.getTelefono());
        dto.setEmail(org.getEmail());

        // Mapeo de organizadores
        dto.setOrganizadores(
                org.getOrganizadores()
                        .stream()
                        .map(UserEntity::getNombre) // Obtener el nombre de cada organizador
                        .collect(Collectors.toList())
        );

        dto.setDescripcion(org.getDescripcion());
        dto.setWebsite(org.getWebsite());
        dto.setDireccion(org.getDireccion());

        // Mapeo de eventos asociados a la organización
        dto.setEventos(
                org.getEventos()
                        .stream()
                        .map(event -> {
                            EventDTO eventDTO = OrganizationMapper.toEventDTO(event);
                            // Si hay un nuevo campo específico a calcular, puedes agregarlo aquí
                            return eventDTO;
                        })
                        .collect(Collectors.toList())
        );

        return dto;
    }


    public static EventDTO toEventDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setId(event.getId());
        dto.setNombre(event.getNombre());
        dto.setFecha(event.getFecha());
        dto.setHorario(event.getHorario());
        dto.setDescripcion(event.getDescripcion());
        dto.setCiudad(event.getCiudad());
        dto.setDireccion(event.getDireccion());
        dto.setEstado(event.getEstado());

        // Lista de nombres de voluntarios asociados al evento
        dto.setVoluntarios(
                event.getVoluntarios()
                        .stream()
                        .map(UserEntity::getId) // Obtener el nombre de cada voluntario
                        .collect(Collectors.toList())
        );

        // Determinar si el usuario está inscrito en el evento
        boolean isUserRegistered = event.getVoluntarios()
                .stream()
                .anyMatch(voluntario -> voluntario.getId().equals(voluntario.getId()));
        dto.setIsUserRegistered(isUserRegistered);

        return dto;
    }

}
