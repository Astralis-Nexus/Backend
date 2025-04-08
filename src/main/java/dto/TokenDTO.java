package dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.model.Account;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    String token;
    String username;
    private Set<String> roles;

    public TokenDTO(String token, Account account) {
        this.token = token;
        this.username = account.getUsername();
        this.roles=account.getRolesAsStrings();
    }
}