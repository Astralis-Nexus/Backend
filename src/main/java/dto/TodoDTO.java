package dto;

import lombok.*;
import persistence.model.Account;
import persistence.model.Todo;

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
    private Todo.Status status;
    private Todo.Source source;
    private String done_by;
    private Integer accountId;
}
