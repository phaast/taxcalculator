package com.bartoszwalter.students.taxes;

/**
 * Represents the type of employment or civil contract used for tax calculation.
 */
public enum ContractType {
    EMPLOYMENT('E', "EMPLOYMENT"),
    CIVIL('C', "CIVIL");

    private final char code;
    private final String description;

    ContractType(char code, String description) {
        this.code = code;
        this.description = description;
    }

    public char getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ContractType fromCode(char code) {
        char upperCode = Character.toUpperCase(code);
        for (ContractType type : values()) {
            if (type.code == upperCode) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown contract type code: " + code);
    }
}
