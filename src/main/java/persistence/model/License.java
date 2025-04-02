package persistence.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "license")
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "pc_number")
    private Integer pcNumber = 0;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    public License(String username, String password, String email, Game game) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.game = game;
    }
}
