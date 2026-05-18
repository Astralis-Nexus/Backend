package controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityControllerTest {

    @Test
    @DisplayName("Secret resolver should use configured secrets with at least 32 bytes.")
    void resolveSecretKeyShouldUseConfiguredSecretWhenLongEnough() {
        String configuredSecret = "01234567890123456789012345678901";

        byte[] secret = SecurityController.resolveSecretKey(configuredSecret);

        assertThat(secret).isEqualTo(configuredSecret.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Secret resolver should generate a 32-byte fallback for missing secrets.")
    void resolveSecretKeyShouldGenerateFallbackForMissingSecret() {
        byte[] secret = SecurityController.resolveSecretKey(null);

        assertThat(secret).hasSize(32);
    }

    @Test
    @DisplayName("Secret resolver should generate a 32-byte fallback for short secrets.")
    void resolveSecretKeyShouldGenerateFallbackForShortSecret() {
        byte[] secret = SecurityController.resolveSecretKey("too-short");

        assertThat(secret).hasSize(32);
    }
}
