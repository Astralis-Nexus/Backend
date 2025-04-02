package dto;

import lombok.*;
import persistence.model.Footer;
import persistence.model.Header;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private String name;
    private List<Header> headers;
    private List<Footer> footers;

    public RoleDTO(String name) {
        this.name = name;
    }
}
