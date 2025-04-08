package dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import persistence.model.Account;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
    private String token;
    private String username;
    private String role;

    public TokenDTO(String token, Account account) {
        this.token = token;
        this.username = account.getUsername();
        this.role=account.getRole().toString();
    }
}