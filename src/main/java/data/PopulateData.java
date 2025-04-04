package data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import persistence.config.HibernateConfig;
import persistence.model.*;
import route.Route;

import java.time.LocalDate;

public class PopulateData {
    public static void populateData(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Role adminRole = new Role(Role.RoleName.ADMIN);
            Role regularRole = new Role(Role.RoleName.REGULAR);
            Role noneRole = new Role(Role.RoleName.NONE);
            em.persist(adminRole);
            em.persist(regularRole);
            em.persist(noneRole);

            em.persist(new Header("Home", regularRole));
            em.persist(new Header("Todos", regularRole));
            em.persist(new Header("QA", regularRole));
            em.persist(new Header("Information", regularRole));
            em.persist(new Header("Contact", regularRole));

            // FOOTER (website bottom area)
            em.persist(new Footer("Contact Us", "Email: support@company.com | Phone: +45 1234 5678", regularRole));
            em.persist(new Footer("About", "This internal tool is for managing your daily work", regularRole));

            Account admin = new Account("admin", "admin", adminRole);
            Account john = new Account("john_doe", "securePassword123", regularRole);
            em.persist(admin);
            em.persist(john);

            em.persist(new Information("All employees must update their passwords by April 15th.", john));
            em.persist(new Information("Team meeting on Monday at 10 AM in Meeting Room B.", john));

            em.persist(new QA("How do I reset my password?", "Click your profile icon and choose 'Reset Password'.", john));
            em.persist(new QA("Where can I see my tasks?", "Navigate to the 'Todos' tab on the dashboard.", john));

            em.persist(new Todo(LocalDate.now().plusDays(1), "Update project documentation", false, john));
            em.persist(new Todo(LocalDate.now().plusDays(2), "Prepare for Monday's team meeting", false, john));
            em.persist(new Todo(LocalDate.now().minusDays(1), "Review PR from Sarah", true, john));

            Game game = new Game("Code Breaker", john);
            em.persist(game);

            em.persist(new License("Code Breaker Pro", "XYZ-9876", "john.doe@example.com", game));

            em.getTransaction().commit();
        }
    }
}
