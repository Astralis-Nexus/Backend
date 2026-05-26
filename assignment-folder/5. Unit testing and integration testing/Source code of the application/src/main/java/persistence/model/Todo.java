package persistence.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(nullable = false)
    private Source source;

    @JsonProperty("done_by")
    @NotBlank
    @Size(min = 1, max = 30)
    @Column(name = "done_by", nullable = false, updatable = false)
    private String doneBy = "";

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @lombok.Generated
    public Todo(LocalDate date, String description, Status status, Source source, String doneBy, Account account) {
        this.date = date;
        this.description = description;
        this.status = status;
        this.source = source;
        this.doneBy = doneBy;
        this.account = account;
    }

    @lombok.Generated
    public Todo(LocalDate date, String description, Status status, Source source, Account account) {
        this.date = date;
        this.description = description;
        this.status = status;
        this.source = source;
        this.account = account;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null.");
        }
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be null or blank.");
        }
        if (description.length() > 255) {
            throw new IllegalArgumentException("Description must be at most 255 characters.");
        }
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        if (status == null) {
            throw new IllegalArgumentException("Status must not be null.");
        }
        this.status = status;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        if (source == null) {
            throw new IllegalArgumentException("Source must not be null.");
        }
        this.source = source;
    }

    public String getDoneBy() {
        return doneBy;
    }

    public void setDoneBy(String doneBy) {
        this.doneBy = ModelValidation.requireTextLength(doneBy, "DoneBy", 1, 30);
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.doneBy = getCurrentUser();
    }

    private String getCurrentUser() {
        return account == null ? doneBy : account.getUsername();
    }

    @PrePersist
    protected void onCreate() {
        this.date = java.time.LocalDate.now();
        if (this.doneBy == null || this.doneBy.isBlank()) {
            this.doneBy = getCurrentUser();
        }
    }
    public enum Source {
        GAMEHUB,
        STORE
    }
    
    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }
}
