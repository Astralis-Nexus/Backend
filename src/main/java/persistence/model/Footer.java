package persistence.model;

import jakarta.persistence.*;
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

    @Size(min = 4)
    @Column(nullable = false)
    private String header;

    @Size(min = 10)
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public Footer(String header, String description, Role role) {
        this.header = header;
        this.description = description;
        this.role = role;
    }
}
