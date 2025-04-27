package persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    @JsonIgnore
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

    public void setPassword(String password) {
        //if (password != null && !password.startsWith("$2a$")) { // Check if already hashed
         //   this.password = BCrypt.hashpw(password, BCrypt.gensalt());
       // } else {
            this.password = password;
        //}
    }

    public Account(String username, String password, Role role) {
        this.username = username;
        this.password = password;
       // this.password = BCrypt.hashpw(password, salt);
        this.role = role;
    }

    public Account(String username, String password) {
        this.username = username;
       // this.password = BCrypt.hashpw(password, salt);
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);

        //return BCrypt.checkpw(password, this.password);
    }

}