package dto;

import lombok.*;

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
}
