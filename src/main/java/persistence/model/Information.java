package persistence.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "information")
public class Information {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false)
    private ImportanceLevel importanceLevel;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Information(String description, Account account, ImportanceLevel importanceLevel) {
        this.description = description;
        this.account = account;
        this.importanceLevel= importanceLevel;
    }

    public enum ImportanceLevel {
        LOW,
        MEDIUM,
        HIGH
    }
    
}
