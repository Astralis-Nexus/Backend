package persistence.model;

final class ModelValidation {
    private ModelValidation() {
    }

    static String requireTextLength(String value, String field, int min, int max) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be null or blank.");
        }
        if (value.length() < min) {
            throw new IllegalArgumentException(field + " must be at least " + min + " characters.");
        }
        if (value.length() > max) {
            throw new IllegalArgumentException(field + " must be at most " + max + " characters.");
        }
        return value;
    }

    static <T> T requireNotNull(T value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " must not be null.");
        }
        return value;
    }

    static Integer requireRange(Integer value, String field, int min, int max) {
        requireNotNull(value, field);
        if (value < min) {
            throw new IllegalArgumentException(field + " must be at least " + min + ".");
        }
        if (value > max) {
            throw new IllegalArgumentException(field + " must be at most " + max + ".");
        }
        return value;
    }
}
