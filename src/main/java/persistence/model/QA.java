package persistence.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank
    @Size(min = 1, max = 255)
    @Column(nullable = false, length = 255)
    private String question;

    @NotBlank
    @Size(min = 1, max = 1000)
    @Column(nullable = false, length = 1000)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @lombok.Generated
    public QA(String question, String answer, Account account) {
        this.question = question;
        this.answer = answer;
        this.account = account;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Question must not be null or blank.");
        }
        if (question.length() > 255) {
            throw new IllegalArgumentException("Question must be at most 255 characters.");
        }
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        if (answer == null || answer.isBlank()) {
            throw new IllegalArgumentException("Answer must not be null or blank.");
        }
        if (answer.length() > 1000) {
            throw new IllegalArgumentException("Answer must be at most 1000 characters.");
        }
        this.answer = answer;
    }
}
