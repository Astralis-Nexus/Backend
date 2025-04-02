package persistence.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "qa")
public class QA {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String question;
    private String answer;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public QA(String question, String answer, Account account) {
        this.question = question;
        this.answer = answer;
        this.account = account;
    }
}
