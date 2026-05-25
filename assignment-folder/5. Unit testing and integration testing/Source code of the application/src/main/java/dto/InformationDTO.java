package dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InformationDTO {
    private Integer id;
    private String description;
    private Integer accountId; 
    private String importanceLevel;

}
