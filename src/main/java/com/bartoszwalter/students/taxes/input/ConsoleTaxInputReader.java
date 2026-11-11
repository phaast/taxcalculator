package com.bartoszwalter.students.taxes.input;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxInput;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.math.BigDecimal;

public class ConsoleTaxInputReader implements TaxInputReader {

    private final BufferedReader reader;
    private final PrintStream promptOutput;

    public ConsoleTaxInputReader(InputStream input, PrintStream promptOutput) {
        this.reader = new BufferedReader(new InputStreamReader(input));
        this.promptOutput = promptOutput;
    }

    @Override
    public TaxInput read() {
        promptOutput.print("Enter income: ");
        BigDecimal income = new BigDecimal(readLine());

        promptOutput.print("Contract Type: (E)mployment, (C)ivil: ");
        ContractType contractType = ContractType.fromCode(readLine().charAt(0));

        return new TaxInput(income, contractType);
    }

    private String readLine() {
        try {
            return reader.readLine().trim();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
