package dto;

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
    private Account account;
}
