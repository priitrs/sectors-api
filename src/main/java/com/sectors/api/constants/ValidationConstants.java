package com.sectors.api.constants;

public final class ValidationConstants {

    private ValidationConstants() {}

    public static final String NAME_PATTERN = "^[\\p{L}\\s'-]+$";
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$";

    public static final String USERNAME_REQUIRED = "Email is required";
    public static final String EMAIL_SIZE = "Email must not exceed {max} characters";
    public static final String EMAIL_INVALID = "Email must be a valid email address";

    public static final String FIRST_NAME_REQUIRED = "First name is required";
    public static final String FIRST_NAME_SIZE = "First name must not exceed {max} characters";
    public static final String FIST_NAME_INVALID = "First name contains invalid characters";

    public static final String LAST_NAME_REQUIRED = "Last name is required";
    public static final String LAST_NAME_SIZE = "Last name must not exceed {max} characters";
    public static final String LAST_NAME_INVALID = "Last name contains invalid characters";

    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_SIZE = "Password must be between {min} and {max} characters";
    public static final String PASSWORD_INVALID = "Password must contain uppercase, lowercase, digit and special character (@$!%*?&)";

    public static final String SECTORS_CANNOT_BE_NULL = "Sectors must be provided";
    public static final String SECTORS_CANNOT_BE_EMPTY = "At least one sector must be selected";
}
