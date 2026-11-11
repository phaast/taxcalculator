package com.bartoszwalter.students.taxes.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ContractTypeTest {

    @ParameterizedTest
    @CsvSource({"E, EMPLOYMENT", "e, EMPLOYMENT", "C, CIVIL", "c, CIVIL"})
    void resolvesContractTypeFromCodeIgnoringCase(char code, ContractType expectedType) {
        assertThat(ContractType.fromCode(code)).isEqualTo(expectedType);
    }

    @Test
    void rejectsUnknownCode() {
        assertThatThrownBy(() -> ContractType.fromCode('Z'))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Z");
    }
}
