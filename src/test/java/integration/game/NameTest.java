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
            "A",
            "AA",
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "Baldur's Gate 3",
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
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
    })
    void createShouldRejectGamesWithInvalidNames(String name) {
        // Given
        Game game = new Game();

        // Then
        assertThatThrownBy(() -> game.setName(name))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("GameDAO should reject duplicate game names.")
    void createShouldRejectDuplicateName() {
        // Given
        Account account = createAccount("game-user");
        gameDAO.create(new Game("BaldursGate3", account));
        Game duplicateGame = new Game("BaldursGate3", account);
        // Then
        assertThatThrownBy(() -> gameDAO.create(duplicateGame))
                .isInstanceOf(RuntimeException.class);
    }

    // ------------------------------ White box positive branches ------------------------------

    @Test
    @DisplayName("GameDAO should keep existing name when update name is null.")
    void whiteBoxUpdateShouldKeepExistingNameWhenNameIsNull() {
        // Given
        Account account = createAccount("game-update-user");
        Game created = gameDAO.create(new Game("OriginalGame", account));
        Game partialUpdate = new Game(
                created.getId(),
                null, // White box: DAO.update Game name null branch.
                account
        );
        // When
        Game updated = gameDAO.update(partialUpdate);
        // Then
        assertThat(updated.getName()).isEqualTo("OriginalGame");
    }
}
