package dto;

import lombok.*;
import persistence.model.Game;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LicenseDTO {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private Integer pcNumber;
    private Game game;
}
