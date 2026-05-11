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
        this.description = ModelValidation.requireTextLength(description, "Description", 10, 500);
    }

    public ImportanceLevel getImportanceLevel() {
        return importanceLevel;
    }

    public void setImportanceLevel(ImportanceLevel importanceLevel) {
        this.importanceLevel = ModelValidation.requireNotNull(importanceLevel, "Importance level");
    }

    public enum ImportanceLevel {
        LOW,
        MEDIUM,
        HIGH
    }
    
}
