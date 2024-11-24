package cl.tomas.voluntariado.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvertToOrganizerRequest {

    private Long userId;
    private Long organizationId;
}

