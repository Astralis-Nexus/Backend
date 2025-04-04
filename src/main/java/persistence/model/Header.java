package persistence.model;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String text;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public Header(String text, Role role) {
        this.text = text;
        this.role = role;
    }
}
