package dto;

import lombok.*;
import persistence.model.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Integer id;
    private String username;
    private String password;
    private Role role;
    private List<Todo> todos;
    private List<Information> information;
    private List<Game> games;
    private List<QA> qas;
}
