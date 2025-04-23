package controller;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dao.AccountDAO;
import dto.AccountDTO;
import dto.TokenDTO;
import exception.ApiException;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManagerFactory;
import persistence.model.Account;
import persistence.model.Role;
import utility.DateUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class SecurityController {

    private static final String SECRET_KEY = "YOUR_SECRET_KEY_HERE_MAKE_IT_LONG_AND_SECURE_32_BYTES";
    private static final String timestamp = DateUtil.getTimestamp();
    private final AccountDAO accountDAO;

    public SecurityController(EntityManagerFactory emf) {
        this.accountDAO = AccountDAO.getInstance(emf);
    }

    public Handler login() {
        return ctx -> {
            AccountDTO input = ctx.bodyAsClass(AccountDTO.class);
            Account verified = accountDAO.verifyLogin(input.getUsername(), input.getPassword());
            if (verified == null) {
                throw new ApiException(401, "Wrong login info.", timestamp);
            } else {
                String token = createToken(verified.getUsername(), verified.getRole());
                ctx.status(200).json(new TokenDTO(token, verified.getUsername(), verified.getRole().getName().toString(), verified.getId()));
            }
        };
    }


    private String createToken(String username, Role role) throws JOSEException {
        // Prepare JWT with claims
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .claim("role", role.getName().toString())
                .claim("username", username)
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + 7200000)) // 2 hours
                .build();

        // Create a signed JWT
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        // Sign the JWT
        signedJWT.sign(new MACSigner(SECRET_KEY));

        return signedJWT.serialize();
    }

    public boolean tokenIsValid(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);

            // Verify the signature
            if (!jwt.verify(new MACVerifier(SECRET_KEY))) {
                throw new ApiException(403, "Invalid token signature", timestamp);
            }

            // Check expiration
            Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();
            if (expirationTime != null && expirationTime.before(new Date())) {
                throw new ApiException(403, "Token has expired", timestamp);
            }

            return true;
        } catch (ParseException e) {
            throw new ApiException(403, "Invalid token format", timestamp);
        } catch (JOSEException e) {
            throw new ApiException(403, "Token verification failed", timestamp);
        }
    }

    public boolean hasRole(String token, String requiredRole) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            if (!tokenIsValid(token)) {
                return false;
            }

            // Get roles from token
            List<String> roles = jwt.getJWTClaimsSet().getStringListClaim("roles");
            return roles != null && roles.contains(requiredRole);
        } catch (Exception e) {
            return false;
        }
    }

    public Handler requireRole(String role, Handler handler) {
        return ctx -> {
            String authHeader = ctx.header("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ApiException(401, "Authorization header missing or invalid", timestamp);
            }

            String token = authHeader.substring("Bearer ".length());
            if (!hasRole(token, role)) {
                throw new ApiException(403, "Access denied. Required role: " + role, timestamp);
            }

            handler.handle(ctx);
        };
    }


    public JWTClaimsSet decodeToken(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            if (tokenIsValid(token)) {
                return jwt.getJWTClaimsSet();
            }
            throw new ApiException(403, "Invalid token", timestamp);
        } catch (ParseException e) {
            throw new ApiException(403, "Failed to decode token", timestamp);
        }
    }
}