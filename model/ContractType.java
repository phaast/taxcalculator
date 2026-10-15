package com.bartoszwalter.students.taxes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ContractType {
    EMPLOYMENT('E', "EMPLOYMENT"),
    CIVIL('C', "CIVIL");

    private final char code;
    private final String description;

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
