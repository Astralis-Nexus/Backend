package persistence.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, updatable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Source source;

    @Column(nullable = false, updatable = false)
    private String done_by = "";

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    //@JsonIgnore

    private Account account;

    public Todo(LocalDate date, String description, Status status, Source source, Account account) {
        this.date = date;
        this.description = description;
        this.status = status;
        this.source = source;
        this.account = account;
    }

    @PreUpdate
    protected void onUpdate() {
        this.done_by = getCurrentUser();
    }

    private String getCurrentUser() {
        return account.getUsername();
    }

    @PrePersist
    protected void onCreate() {
        this.date = java.time.LocalDate.now();
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
