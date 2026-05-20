package dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import persistence.model.Account;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QADTO {
    private Integer id;
    private String question;
    private String answer;
    private Integer accountId;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Account account;

    public QADTO(Integer id, String question, String answer, Integer accountId) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.accountId = accountId;
    }

    public Integer getAccountId() {
        if (accountId != null) {
            return accountId;
        }
        if (account == null) {
            return null;
        }
        return account.getId();
    }
}
