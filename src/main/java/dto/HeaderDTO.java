package dto;

import lombok.*;
import persistence.model.Role;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeaderDTO {
    private Integer id;
    private String name;
    private Role role;
}
