package dao;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistence.model.Account;
import persistence.model.Game;
import persistence.model.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest extends BaseTest {

    @Test
    @DisplayName("Get all the games.")
    public void getAll() {
        // Given
        int expectedSize = 1;

        // When
        List<Game> games = gameDAO.getAll();

        // Then
        assertFalse(games.isEmpty());
        assertEquals(expectedSize, games.size());
    }

    @Test
    @DisplayName("Create an game with existing account.")
    public void create() {
        // Given
        Account account2 = new Account("username", "password", new Role(Role.RoleName.REGULAR));
        accountDAO.create(account2);
        Game gameToCreate = new Game("username1", account2);
        int expectedId = 2;

        // When
        Game gameCreated = gameDAO.create(gameToCreate);

        // Then
        assertNotNull(gameCreated);
        assertEquals(expectedId, gameCreated.getId());
    }

    @Test
    @DisplayName("Update an existing game.")
    public void update() {
        // Given
        Game gameToUpdate;
        int givenId = 1;

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            gameToUpdate = em.find(Game.class, givenId);
            em.getTransaction().commit();
        }

        // When
        gameToUpdate.setName("test1)");
        Game gameUpdated = gameDAO.update(gameToUpdate);

        // Then
        assertNotNull(gameUpdated);
        assertEquals(givenId, gameUpdated.getId());
    }

    @Test
    @DisplayName("Get an specific game.")
    public void getById() {
        // Given
        int givenId = 1;

        // When
        Game gameFound = gameDAO.getById(givenId);

        // Then
        assertNotNull(gameFound);
        assertEquals(givenId, gameFound.getId());
    }

    @Test
    @DisplayName("Delete an specific game.")
    public void delete() {
        // Given
        int givenId = 1;

        // When
        Game deletedGame = gameDAO.delete(givenId);

        // Then
        assertNotNull(deletedGame);
        assertEquals(givenId, deletedGame.getId());
    }
}