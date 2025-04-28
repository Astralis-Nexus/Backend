package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDTO {
    private Integer id;
    private String username;
    private String password;
    private Role role;
    private List<Todo> todos;
    private List<Information> information;
    private List<Game> games;
    private List<QA> qas;

    public AccountDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
        this.role = account.getRole();
        this.todos = account.getTodos();
        this.information = account.getInformations();
        this.games = account.getGames();
        this.qas = account.getQas();
        this.password=account.getPassword();
    }

    public Set<String> getRoles() {
        Set<String> roles = new HashSet<>();
        if (role != null) {
            roles.add(role.getName().toString());
        }
        return roles;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", todos=" + todos +
                ", information=" + information +
                ", games=" + games +
                ", qas=" + qas +
                '}';
    }
}
