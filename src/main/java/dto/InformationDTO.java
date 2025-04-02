package dto;

import lombok.*;
import persistence.model.Account;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InformationDTO {
    private Integer id;
    private String description;
    private Account account;
}
