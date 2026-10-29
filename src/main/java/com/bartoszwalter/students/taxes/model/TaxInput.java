package com.bartoszwalter.students.taxes.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaxInput {

    private final BigDecimal income;
    private final ContractType contractType;
}
