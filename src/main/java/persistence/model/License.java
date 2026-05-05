package persistence.model;

import jakarta.validation.constraints.Size;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "game")
@Table(name = "license")
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 1)
    @Column(nullable = false, unique = true)
    private String username;

    @Size(min = 8)
    @Column(nullable = false)
    private String password;

    @Size(min = 6)
    @Column(nullable = false, unique = true)
    private String email;

    @Size(min = 0)
    @Column(name = "pc_number")
    private Integer pcNumber = 0;

    @ManyToOne
    @JoinColumn(name = "game_id")
     //@JsonIgnore
    private Game game;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LicenseStatus status;


    public License(String username, String password, String email, LicenseStatus status, Game game) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.game = game;
        this.status = status;
    }

    public License(String username, String password, String email, Integer pcNumber,  Game game ) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.pcNumber = pcNumber;
        this.game = game;
    }
    
    public License(String username, String password, String email, Integer pcNumber, Game game, LicenseStatus status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.pcNumber = pcNumber;
        this.game = game;
        this.status = status;
    }
    public enum LicenseStatus {
        ACTIVE,
        INACTIVE
    }
}
