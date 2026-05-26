package persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "footer")
public class Footer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 4, max = 50)
    @Column(nullable = false, length = 50)
    private String header;

    @NotBlank
    @Size(min = 10, max = 255)
    @Column(nullable = false, length = 255)
    private String description;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @lombok.Generated
    public Footer(String header, String description, Role role) {
        this.header = header;
        this.description = description;
        this.role = role;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = ModelValidation.requireTextLength(header, "Header", 4, 50);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = ModelValidation.requireTextLength(description, "Description", 10, 255);
    }
}
