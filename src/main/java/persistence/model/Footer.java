package persistence.model;

import jakarta.persistence.*;
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

    private String header;
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public Footer(String header, String description, Role role) {
        this.header = header;
        this.description = description;
        this.role = role;
    }
}
