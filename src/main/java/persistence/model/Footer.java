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
        if (header == null || header.isBlank()) {
            throw new IllegalArgumentException("Header must not be null or blank.");
        }
        if (header.length() < 4) {
            throw new IllegalArgumentException("Header must be at least 4 characters.");
        }
        if (header.length() > 50) {
            throw new IllegalArgumentException("Header must be at most 50 characters.");
        }
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be null or blank.");
        }
        if (description.length() < 10) {
            throw new IllegalArgumentException("Description must be at least 10 characters.");
        }
        if (description.length() > 255) {
            throw new IllegalArgumentException("Description must be at most 255 characters.");
        }
        this.description = description;
    }
}
