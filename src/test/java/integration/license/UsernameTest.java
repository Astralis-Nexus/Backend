package integration.license;

import integration.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Game;
import persistence.model.License;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UsernameTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("LicenseDAO should persist licenses with valid username lengths.")
    @ValueSource(strings = {
            "A",
            "AA",
            "AAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "steam_user_42",
    })
    void createShouldPersistLicensesWithValidUsernameLengths(String username) {
        // Given
        License license = validLicense();
        license.setUsername(username);

        // When
        License created = licenseDAO.create(license);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getUsername()).isEqualTo(username).hasSizeBetween(1, 30);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("LicenseDAO should reject licenses with invalid usernames.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
    })

    void createShouldRejectLicensesWithInvalidUsernames(String username) {
        // Given
        License license = new License();

        // Then
        assertThatThrownBy(() -> license.setUsername(username))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @Test
    @DisplayName("LicenseDAO should reject duplicate usernames.")
    void createShouldRejectDuplicateUsername() {
        // Given
        Game game = createGame("license-game");
        licenseDAO.create(validLicense(game));
        License duplicateLicense = validLicense(game);

        // Then
        assertThatThrownBy(() -> licenseDAO.create(duplicateLicense))
                .isInstanceOf(RuntimeException.class);
    }

    // ------------------------------ Positive branches ------------------------------

    @Test
    @DisplayName("LicenseDAO should cover update value branches and find an existing game.")
    void shouldCoverUpdateBranchesAndFindExistingGame() {
        // Given
        Game firstGame = createGame("license-update-game");
        License firstLicense = licenseDAO.create(validLicense(firstGame));
        License partialUpdate = new License(
                firstLicense.getId(),
                null, // White box: DAO.update License username null branch.
                null, // White box: DAO.update License password null branch.
                null, // White box: DAO.update License email null branch.
                firstLicense.getPcNumber() + 1, // White box: DAO.update License pcNumber mismatch branch.
                firstGame,
                License.LicenseStatus.ACTIVE
        );

        Game secondGame = gameDAO.create(new Game("license-provided-update-game", createAccount("license-game-owner-2")));
        License secondLicense = licenseDAO.create(validLicense(
                secondGame,
                "steam_user_43",
                "player-three@example.com"
        ));

        License fullUpdate = new License(
                secondLicense.getId(),
                "new_steam_user", // White box: DAO.update License username present branch.
                "NewLic@2026!", // White box: DAO.update License password present branch.
                "updated@example.com", // White box: DAO.update License email present branch.
                secondLicense.getPcNumber(), // White box: DAO.update License pcNumber equal branch.
                secondGame,
                License.LicenseStatus.ACTIVE
        );

        // When
        License unchanged = licenseDAO.update(partialUpdate);
        License updated = licenseDAO.update(fullUpdate);

        // Then
        assertThat(unchanged.getUsername()).isEqualTo("steam_user_42");
        assertThat(unchanged.getPassword()).isEqualTo("Lic@2026!");
        assertThat(unchanged.getEmail()).isEqualTo("player-two@example.com");
        assertThat(unchanged.getPcNumber()).isEqualTo(firstLicense.getPcNumber());
        assertThat(updated.getUsername()).isEqualTo("new_steam_user");
        assertThat(updated.getPassword()).isEqualTo("NewLic@2026!");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");
        assertThat(updated.getPcNumber()).isEqualTo(secondLicense.getPcNumber());
        assertThat(licenseDAO.getGameById(secondGame.getId())).isNotNull();
    }

    // ------------------------------ Negative branches ------------------------------

    @Test
    @DisplayName("LicenseDAO should return null for missing game.")
    void getGameByIdShouldReturnNullForMissingGame() {
        // Then
        assertThat(licenseDAO.getGameById(404)).isNull(); // White box: LicenseDAO missing game branch.
    }

    private License validLicense() {
        License license = new License();
        license.setUsername("steam_user_42");
        license.setPassword("Lic@2026!");
        license.setEmail("player@example.com");
        license.setPcNumber(1);
        license.setStatus(License.LicenseStatus.ACTIVE);
        license.setGame(createGame("license-game"));
        return license;
    }

    private License validLicense(Game game) {
        return validLicense(game, "steam_user_42", "player-two@example.com");
    }

    private License validLicense(Game game, String username, String email) {
        License license = new License();
        license.setUsername(username);
        license.setPassword("Lic@2026!");
        license.setEmail(email);
        license.setPcNumber(1);
        license.setStatus(License.LicenseStatus.ACTIVE);
        license.setGame(game);
        return license;
    }
}
