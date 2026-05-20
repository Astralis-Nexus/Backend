package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private Integer accountId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Account account;

    public Integer getAccountId() {
        return accountId != null ? accountId : account == null ? null : account.getId();
    }
}
