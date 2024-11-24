package cl.tomas.voluntariado.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserUpdateDTO {

    private Long userId;
    private Long organizationId;
    private List<Long> roles;
}
