package integration.game;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Account;
import persistence.model.Game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("GameDAO should persist games with valid name lengths.")
    @ValueSource(strings = {
            "A", // White box value: min boundary
            "AA", // White box value: just above min
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just below max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: max boundary
            "Baldur's Gate 3"
    })
    void createShouldPersistGamesWithValidNameLengths(String name) {
        // Given
        Account account = createAccount("game-user");
        Game game = new Game();
        game.setName(name);
        game.setAccount(account);

        // When
        Game created = gameDAO.create(game);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo(name).hasSizeBetween(1, 100);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("GameDAO should reject games with invalid names.")
    // White box values: null and empty string branches.
    @NullAndEmptySource
    @ValueSource(strings = {
            " ", // White box value: blank branch
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", // White box value: just above max
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    })
    void createShouldRejectGamesWithInvalidNames(String name) {
        // Then
        assertThatThrownBy(() -> {
            Account account = createAccount("game-user");
            Game game = new Game();
            game.setName(name);
            game.setAccount(account);
            gameDAO.create(game);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("GameDAO should reject duplicate game names.")
    void createShouldRejectDuplicateName() {
        // Given
        Account account = createAccount("game-user");
        gameDAO.create(new Game("BaldursGate3", account));

        // Then
        assertThatThrownBy(() -> gameDAO.create(new Game("BaldursGate3", account)))
                .isInstanceOf(RuntimeException.class);
    }
}
