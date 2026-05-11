package persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 1, max = 30)
    @Column(nullable = false, unique = true, length = 30)
    private String username;

    @NotBlank
    @Size(min = 8, max = 128)
    @Column(nullable = false, length = 128)
    private String password;

    @NotBlank
    @Size(min = 6, max = 254)
    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @NotNull
    @Min(0)
    @Column(name = "pc_number")
    private Integer pcNumber = 0;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LicenseStatus status;


    @lombok.Generated
    public License(String username, String password, String email, LicenseStatus status, Game game) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.game = game;
        this.status = status;
    }

    @lombok.Generated
    public License(String username, String password, String email, Integer pcNumber,  Game game ) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.pcNumber = pcNumber;
        this.game = game;
    }
    
    @lombok.Generated
    public License(String username, String password, String email, Integer pcNumber, Game game, LicenseStatus status) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.pcNumber = pcNumber;
        this.game = game;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username must not be null or blank.");
        }
        if (username.length() > 30) {
            throw new IllegalArgumentException("Username must be at most 30 characters.");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password must not be null or blank.");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters.");
        }
        if (password.length() > 128) {
            throw new IllegalArgumentException("Password must be at most 128 characters.");
        }
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must not be null or blank.");
        }
        if (email.length() < 6) {
            throw new IllegalArgumentException("Email must be at least 6 characters.");
        }
        if (email.length() > 254) {
            throw new IllegalArgumentException("Email must be at most 254 characters.");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Email must contain @.");
        }
        if (!email.contains(".")) {
            throw new IllegalArgumentException("Email must contain a dot.");
        }
        if (email.startsWith("@")) {
            throw new IllegalArgumentException("Email must not start with @.");
        }
        if (email.endsWith("@")) {
            throw new IllegalArgumentException("Email must not end with @.");
        }
        this.email = email;
    }

    public Integer getPcNumber() {
        return pcNumber;
    }

    public void setPcNumber(Integer pcNumber) {
        if (pcNumber == null) {
            throw new IllegalArgumentException("PcNumber must not be null.");
        }
        if (pcNumber < 0) {
            throw new IllegalArgumentException("PcNumber must be at least 0.");
        }
        if (pcNumber > 20) {
            throw new IllegalArgumentException("PcNumber must be at most 20.");
        }
        this.pcNumber = pcNumber;
    }

    public LicenseStatus getStatus() {
        return status;
    }

    public void setStatus(LicenseStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null.");
        }
        this.status = status;
    }

    public enum LicenseStatus {
        ACTIVE,
        INACTIVE
    }
}
