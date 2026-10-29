package com.bartoszwalter.students.taxes.input;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bartoszwalter.students.taxes.model.ContractType;
import com.bartoszwalter.students.taxes.model.TaxInput;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class ConsoleTaxInputReaderTest {

    private final ByteArrayOutputStream prompts = new ByteArrayOutputStream();

    @Test
    void readsIncomeAndContractType() {
        TaxInput input = readerFor(" 1000.50 \n c \n").read();

        assertThat(input.getIncome()).isEqualByComparingTo("1000.50");
        assertThat(input.getContractType()).isEqualTo(ContractType.CIVIL);
    }

    @Test
    void promptsForEachValue() {
        readerFor("1000\nE\n").read();

        assertThat(prompts.toString(StandardCharsets.UTF_8))
                .isEqualTo("Enter income: Contract Type: (E)mployment, (C)ivil: ");
    }

    @Test
    void rejectsIncomeThatIsNotANumber() {
        assertThatThrownBy(() -> readerFor("abc\nE\n").read()).isInstanceOf(NumberFormatException.class);
    }

    @Test
    void rejectsUnknownContractCode() {
        assertThatThrownBy(() -> readerFor("1000\nZ\n").read()).isInstanceOf(IllegalArgumentException.class);
    }

    private ConsoleTaxInputReader readerFor(String consoleInput) {
        return new ConsoleTaxInputReader(
                new ByteArrayInputStream(consoleInput.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(prompts, true, StandardCharsets.UTF_8));
    }
}
