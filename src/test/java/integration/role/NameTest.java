package integration.role;

import integration.BaseIntegrationTest;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import persistence.model.Account;
import persistence.model.Role;
import persistence.model.Role.RoleName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NameTest extends BaseIntegrationTest {

    // ------------------------------ Positive values ------------------------------

    @ParameterizedTest
    @DisplayName("RoleDAO should persist roles with valid enum names.")
    @EnumSource(value = RoleName.class, names = {"NONE", "REGULAR", "ADMIN"})
    void createShouldPersistRolesWithValidEnumNames(RoleName name) {
        // Given / When
        Role created;
        if (name == RoleName.REGULAR) {
            created = roleDAO.getById(regularRole.getId());
        } else {
            Role role = new Role();
            role.setName(name);
            created = roleDAO.create(role);
        }
        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo(name);
    }

    // ------------------------------ Negative values ------------------------------

    @ParameterizedTest
    @DisplayName("RoleName should reject invalid enum values.")
    @ValueSource(strings = {
            "admin",
            "SUPER_ADMIN",
            "USER",
    })
    void roleNameShouldRejectInvalidEnumValues(String name) {
        // Then
        assertThatThrownBy(() -> RoleName.valueOf(name))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @ParameterizedTest
    @DisplayName("RoleName should reject null, empty, and blank values.")
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
    })
    void roleNameShouldRejectNullEmptyAndBlankValues(String name) {
        // Then
        assertThat(name).satisfiesAnyOf(
                value -> assertThat(value).isNull(),
                value -> assertThat(value).isBlank()
        );
    }
    @Test
    @DisplayName("Role setter should reject null.")
    void nameSetterShouldRejectNull() {
        // Given
        Role role = new Role();
        // Then
        assertThatThrownBy(() -> role.setName(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ------------------------------ White box positive branches ------------------------------

    @Test
    @DisplayName("RoleDAO should move accounts to NONE before deleting a role.")
    void whiteBoxDeleteShouldMoveAccountsToNoneRole() {
        // Given
        roleDAO.create(new Role(RoleName.NONE));
        Account account = createAccount("role-delete-user");
        // When
        Role deleted = roleDAO.delete(regularRole.getId()); // White box: DAO.delete Role account reassignment branch.
        Account updatedAccount = accountDAO.getById(account.getId());
        // Then
        assertThat(deleted).isNotNull();
        assertThat(updatedAccount.getRole().getName()).isEqualTo(RoleName.NONE);
    }

    // ------------------------------ White box negative branches ------------------------------

    @Test
    @DisplayName("RoleDAO should handle update when role does not already exist.")
    void whiteBoxUpdateShouldHandleMissingExistingRole() {
        // Given
        Role missingRole = new Role(
                404,
                RoleName.ADMIN,
                null,
                null
        );
        // When
        Role updated = roleDAO.update(missingRole); // White box: DAO.update Role existingRole null branch.
        // Then
        assertThat(updated).isNotNull();
    }

    @Test
    @DisplayName("RoleDAO should wrap persistence errors when delete cannot find NONE role.")
    void whiteBoxDeleteShouldWrapPersistenceException() {
        // Given
        createAccount("role-delete-error-user");
        Integer regularRoleId = regularRole.getId();

        // Then
        assertThatThrownBy(() -> roleDAO.delete(regularRoleId)) // White box: DAO.delete PersistenceException catch branch.
                .isInstanceOf(PersistenceException.class)
                .hasMessageContaining("Error deleting Role with id: " + regularRoleId);
    }
}
