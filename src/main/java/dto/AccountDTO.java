package dto;

import lombok.*;
import persistence.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.role = account.getRole();
        this.todos = account.getTodos();
        this.information = account.getInformations();
        this.games = account.getGames();
        this.qas = account.getQas();
    }

    public Set<String> getRoles() {
        Set<String> roles = new HashSet<>();
        if (role != null) {
            roles.add(role.getName().toString());
        }
        return roles;
    }
}
