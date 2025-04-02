package dto;

import lombok.*;
import persistence.model.Account;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {
    private Integer id;
    private LocalDate date;
    private String description;
    private boolean status;
    private String done_by;
    private Account account;
}
