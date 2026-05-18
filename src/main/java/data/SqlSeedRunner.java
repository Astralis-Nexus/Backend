package data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SqlSeedRunner {
    private static final String SEED_FILE = "seed.sql";

    private SqlSeedRunner() {
    }

    public static void run(EntityManagerFactory emf) {
        String seedSql = readSeedSql();

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            for (String statement : seedSql.split(";")) {
                String sql = statement.trim();
                if (!sql.isBlank()) {
                    em.createNativeQuery(sql).executeUpdate();
                }
            }

            em.getTransaction().commit();
        }
    }

    private static String readSeedSql() {
        try (InputStream stream = SqlSeedRunner.class.getClassLoader().getResourceAsStream(SEED_FILE)) {
            if (stream == null) {
                throw new IllegalStateException(SEED_FILE + " was not found on the classpath.");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append(System.lineSeparator());
                }
                return builder.toString();
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read " + SEED_FILE + ".", exception);
        }
    }
}
