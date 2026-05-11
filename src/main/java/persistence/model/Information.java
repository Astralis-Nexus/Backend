package persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 10, max = 500)
    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING) 
    @NotNull
    @Column(nullable = false)
    private ImportanceLevel importanceLevel;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @lombok.Generated
    public Information(String description, Account account, ImportanceLevel importanceLevel) {
        this.description = description;
        this.account = account;
        this.importanceLevel= importanceLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be null or blank.");
        }
        if (description.length() < 10) {
            throw new IllegalArgumentException("Description must be at least 10 characters.");
        }
        if (description.length() > 500) {
            throw new IllegalArgumentException("Description must be at most 500 characters.");
        }
        this.description = description;
    }

    public ImportanceLevel getImportanceLevel() {
        return importanceLevel;
    }

    public void setImportanceLevel(ImportanceLevel importanceLevel) {
        if (importanceLevel == null) {
            throw new IllegalArgumentException("Importance level must not be null.");
        }
        this.importanceLevel = importanceLevel;
    }

    public enum ImportanceLevel {
        LOW,
        MEDIUM,
        HIGH
    }
    
}
