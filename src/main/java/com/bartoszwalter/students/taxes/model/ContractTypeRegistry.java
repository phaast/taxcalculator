package com.bartoszwalter.students.taxes.model;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public final class ContractTypeRegistry<T extends ContractTypeAware> {

    private final Map<ContractType, T> implementations = new EnumMap<>(ContractType.class);

    public ContractTypeRegistry(Collection<? extends T> implementations) {
        implementations.forEach(this::register);
    }

    public T get(ContractType contractType) {
        T implementation = implementations.get(contractType);
        if (implementation == null) {
            throw new IllegalArgumentException("No implementation registered for contract type: " + contractType);
        }
        return implementation;
    }

    private void register(T implementation) {
        ContractType contractType = implementation.getContractType();
        if (implementations.putIfAbsent(contractType, implementation) != null) {
            throw new IllegalArgumentException("Duplicate implementation registered for contract type: " + contractType);
        }
    }
}
