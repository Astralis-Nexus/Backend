package controller;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import exception.ApiException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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

    @Test
    @DisplayName("Token validation should accept valid tokens.")
    void tokenIsValidShouldAcceptValidTokens() throws Exception {
        SecurityController controller = new SecurityController(null);
        String token = createSignedToken(getSecretKey(), new Date(System.currentTimeMillis() + 7200000));

        assertThat(controller.tokenIsValid(token)).isTrue();
    }

    @Test
    @DisplayName("Token validation should accept tokens without expiration.")
    void tokenIsValidShouldAcceptTokensWithoutExpiration() throws Exception {
        SecurityController controller = new SecurityController(null);
        String token = createSignedToken(getSecretKey(), null);

        assertThat(controller.tokenIsValid(token)).isTrue();
    }

    @Test
    @DisplayName("Role check should return true when token contains the required role.")
    void hasRoleShouldReturnTrueWhenTokenContainsRequiredRole() throws Exception {
        SecurityController controller = new SecurityController(null);
        String token = createSignedTokenWithRoles(
                getSecretKey(),
                new Date(System.currentTimeMillis() + 7200000),
                List.of("REGULAR", "ADMIN")
        );

        assertThat(controller.hasRole(token, "ADMIN")).isTrue();
    }

    @Test
    @DisplayName("Role check should return false when token has no roles claim.")
    void hasRoleShouldReturnFalseWhenRolesClaimIsMissing() throws Exception {
        SecurityController controller = new SecurityController(null);
        String token = createSignedToken(getSecretKey(), new Date(System.currentTimeMillis() + 7200000));

        assertThat(controller.hasRole(token, "ADMIN")).isFalse();
    }

    @Test
    @DisplayName("Role check should return false for malformed tokens.")
    void hasRoleShouldReturnFalseForMalformedTokens() {
        SecurityController controller = new SecurityController(null);

        assertThat(controller.hasRole("not-a-token", "ADMIN")).isFalse();
    }

    @Test
    @DisplayName("Decode token should return claims for valid tokens.")
    void decodeTokenShouldReturnClaimsForValidTokens() throws Exception {
        SecurityController controller = new SecurityController(null);
        String token = createSignedToken(getSecretKey(), new Date(System.currentTimeMillis() + 7200000));

        JWTClaimsSet claimsSet = controller.decodeToken(token);

        assertThat(claimsSet.getSubject()).isEqualTo("seed-user");
        assertThat(claimsSet.getStringClaim("username")).isEqualTo("seed-user");
    }

    @Test
    @DisplayName("Decode token should reject malformed tokens.")
    void decodeTokenShouldRejectMalformedTokens() {
        SecurityController controller = new SecurityController(null);

        assertThatThrownBy(() -> controller.decodeToken("not-a-token"))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Failed to decode token");
    }

    @Test
    @DisplayName("Require role should call handler when token has the required role.")
    void requireRoleShouldCallHandlerForAllowedRole() throws Exception {
        SecurityController controller = new SecurityController(null);
        String token = createSignedTokenWithRoles(
                getSecretKey(),
                new Date(System.currentTimeMillis() + 7200000),
                List.of("ADMIN")
        );
        AtomicBoolean handlerWasCalled = new AtomicBoolean(false);
        Handler protectedHandler = ctx -> handlerWasCalled.set(true);

        controller.requireRole("ADMIN", protectedHandler).handle(contextWithAuthorization("Bearer " + token));

        assertThat(handlerWasCalled).isTrue();
    }

    @Test
    @DisplayName("Require role should reject missing authorization header.")
    void requireRoleShouldRejectMissingAuthorizationHeader() {
        SecurityController controller = new SecurityController(null);

        assertThatThrownBy(() -> controller.requireRole("ADMIN", ctx -> {
        }).handle(contextWithAuthorization(null)))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Authorization header missing or invalid");
    }

    @Test
    @DisplayName("Require role should reject invalid authorization header.")
    void requireRoleShouldRejectInvalidAuthorizationHeader() {
        SecurityController controller = new SecurityController(null);

        assertThatThrownBy(() -> controller.requireRole("ADMIN", ctx -> {
        }).handle(contextWithAuthorization("Token abc")))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Authorization header missing or invalid");
    }

    @Test
    @DisplayName("Require role should reject tokens without the required role.")
    void requireRoleShouldRejectWrongRole() throws Exception {
        SecurityController controller = new SecurityController(null);
        String token = createSignedTokenWithRoles(
                getSecretKey(),
                new Date(System.currentTimeMillis() + 7200000),
                List.of("REGULAR")
        );

        assertThatThrownBy(() -> controller.requireRole("ADMIN", ctx -> {
        }).handle(contextWithAuthorization("Bearer " + token)))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Access denied. Required role: ADMIN");
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

    private static String createSignedTokenWithRoles(byte[] secretKey, Date expirationTime, List<String> roles) throws Exception {
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("seed-user")
                .claim("roles", roles)
                .claim("username", "seed-user")
                .issueTime(new Date())
                .expirationTime(expirationTime)
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(new MACSigner(secretKey));
        return signedJWT.serialize();
    }

    private static Context contextWithAuthorization(String authorizationHeader) {
        return (Context) Proxy.newProxyInstance(
                Context.class.getClassLoader(),
                new Class[]{Context.class},
                (proxy, method, args) -> {
                    if ("header".equals(method.getName())) {
                        return authorizationHeader;
                    }
                    if (method.getReturnType().equals(Context.class)) {
                        return proxy;
                    }
                    return null;
                }
        );
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
