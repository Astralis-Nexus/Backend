package persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"todos", "informations", "games", "qas"})

@AllArgsConstructor
@Table(name = "account")

public class Account {
    @Transient
    String salt = BCrypt.gensalt();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    @Size(min = 1, max = 30)
    @Column(unique = true, nullable = false, length = 30)
    private String username;
    @NotBlank
    @Size(min = 8, max = 128)
    @Column(nullable = false, length = 128)
    private String password;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_name", nullable = false)
    private Role role;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Todo> todos;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Information> informations;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Game> games;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<QA> qas;

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

    @lombok.Generated
    public Account(String username, String password, Role role) {
        this.username = username;
        setPassword( password);
        this.role = role;
    }

    @lombok.Generated
    public Account(String username) {
        this.username = username;
    }

    @lombok.Generated
    public Account(String username, String password) {
        this.username = username;
        setPassword( password);
    }

    public boolean verifyPassword(String password) {
       return this.password != null && this.password.equals(password);

    }
}
