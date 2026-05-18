package controller;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import exception.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    @DisplayName("Token validation should reject malformed tokens.")
    void tokenIsValidShouldRejectMalformedTokens() {
        SecurityController controller = new SecurityController(null);

        assertThatThrownBy(() -> controller.tokenIsValid("not-a-token"))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Invalid token format");
    }

    @Test
    @DisplayName("Token validation should reject tokens with invalid signatures.")
    void tokenIsValidShouldRejectInvalidSignatures() throws Exception {
        SecurityController controller = new SecurityController(null);
        String token = createSignedToken(
                getDifferentSecretKey(),
                new Date(System.currentTimeMillis() + 7200000)
        );

        assertThatThrownBy(() -> controller.tokenIsValid(token))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Invalid token signature");
    }

    @Test
    @DisplayName("Token validation should reject expired tokens.")
    void tokenIsValidShouldRejectExpiredTokens() throws Exception {
        SecurityController controller = new SecurityController(null);
        String token = createSignedToken(getSecretKey(), new Date(System.currentTimeMillis() - 1000));

        assertThatThrownBy(() -> controller.tokenIsValid(token))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Token has expired");
    }

    private static String createSignedToken(byte[] secretKey, Date expirationTime) throws Exception {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("seed-user")
                .claim("role", "REGULAR")
                .claim("username", "seed-user")
                .issueTime(new Date())
                .expirationTime(expirationTime)
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(new MACSigner(secretKey));
        return signedJWT.serialize();
    }

    private static byte[] getSecretKey() throws Exception {
        Field field = SecurityController.class.getDeclaredField("SECRET_KEY");
        field.setAccessible(true);
        return (byte[]) field.get(null);
    }

    private static byte[] getDifferentSecretKey() throws Exception {
        byte[] differentSecretKey = getSecretKey().clone();
        differentSecretKey[0] = (byte) (differentSecretKey[0] ^ 1);
        return differentSecretKey;
    }
}
