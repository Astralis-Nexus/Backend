package dto;

import lombok.*;
import persistence.model.Role;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FooterDTO {
    private Integer id;
    private String header;
    private String description;
    private Role role;
}
