package com.bartoszwalter.students.taxes.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;

class ContractTypeRegistryTest {

    private final ContractTypeAware employmentImplementation = () -> ContractType.EMPLOYMENT;
    private final ContractTypeAware civilImplementation = () -> ContractType.CIVIL;

    @Test
    void returnsImplementationRegisteredForContractType() {
        ContractTypeRegistry<ContractTypeAware> registry =
                new ContractTypeRegistry<>(List.of(employmentImplementation, civilImplementation));

        assertThat(registry.get(ContractType.EMPLOYMENT)).isSameAs(employmentImplementation);
        assertThat(registry.get(ContractType.CIVIL)).isSameAs(civilImplementation);
    }

    @Test
    void rejectsContractTypeWithoutImplementation() {
        ContractTypeRegistry<ContractTypeAware> registry = new ContractTypeRegistry<>(List.of(employmentImplementation));

        assertThatThrownBy(() -> registry.get(ContractType.CIVIL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CIVIL");
    }

    @Test
    void rejectsTwoImplementationsForSameContractType() {
        ContractTypeAware secondEmploymentImplementation = () -> ContractType.EMPLOYMENT;

        assertThatThrownBy(() -> new ContractTypeRegistry<>(List.of(employmentImplementation, secondEmploymentImplementation)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("EMPLOYMENT");
    }
}
