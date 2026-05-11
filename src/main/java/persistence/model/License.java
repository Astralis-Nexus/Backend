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
        this.username = ModelValidation.requireTextLength(username, "Username", 1, 30);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = ModelValidation.requireTextLength(password, "Password", 8, 128);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        ModelValidation.requireTextLength(email, "Email", 6, 254);
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
        this.pcNumber = ModelValidation.requireRange(pcNumber, "PcNumber", 0, 20);
    }

    public LicenseStatus getStatus() {
        return status;
    }

    public void setStatus(LicenseStatus status) {
        this.status = ModelValidation.requireNotNull(status, "Status");
    }

    public enum LicenseStatus {
        ACTIVE,
        INACTIVE
    }
}
