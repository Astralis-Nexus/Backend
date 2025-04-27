package dto;

import lombok.*;
import persistence.model.Game;
import persistence.model.License;

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
    private License.LicenseStatus status;
}
