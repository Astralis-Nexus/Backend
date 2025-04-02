package dto;

import lombok.*;
import persistence.model.Account;
import persistence.model.License;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameDTO {
    private Integer id;
    private String name;
    private List<License> licenses;
    private Account account;
}
