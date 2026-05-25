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
@Table(name = "header")
public class Header {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Size(min = 1, max = 80)
    @Column(nullable = false, length = 80)
    private String text;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @lombok.Generated
    public Header(String text, Role role) {
        this.text = text;
        this.role = role;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Text must not be null or blank.");
        }
        if (text.length() > 80) {
            throw new IllegalArgumentException("Text must be at most 80 characters.");
        }
        this.text = text;
    }
}
