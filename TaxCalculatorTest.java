package com.bartoszwalter.students.taxes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

public class TaxCalculatorTest {

    public static void main(String[] args) {
        testDirectServiceCalculations();
        testEmploymentContractWith1000();
        testEmploymentContractWith5000();
        testCivilContractWith1000();
        testCivilContractWith5000();
        testInvalidContractThrowsException();
        System.out.println("ALL CHARACTERIZATION & UNIT TESTS PASSED SUCCESSFULLY!");
    }

    private static void testDirectServiceCalculations() {
        TaxCalculatorService service = new TaxCalculatorService();
        TaxCalculationResult resultE = service.calculate(ContractType.EMPLOYMENT, new BigDecimal("1000.00"));

        assertEquals(resultE.getGrossIncome(), new BigDecimal("1000.00"));
        assertEquals(resultE.getPensionContribution(), new BigDecimal("97.60"));
        assertEquals(resultE.getDisabilityContribution(), new BigDecimal("15.00"));
        assertEquals(resultE.getSicknessContribution(), new BigDecimal("24.50"));
        assertEquals(resultE.getTotalSocialSecurity(), new BigDecimal("137.10"));
        assertEquals(resultE.getHealthContribution(), new BigDecimal("90.00"));
        assertEquals(resultE.getTaxDeductibleHealthContribution(), new BigDecimal("77.50"));
        assertEquals(resultE.getTaxDeductibleExpenses(), new BigDecimal("111.25"));
        assertEquals(resultE.getTaxableBase(), new BigDecimal("888.75"));
        assertEquals(resultE.getRoundedTaxableBase(), new BigDecimal("889"));
        assertEquals(resultE.getAdvanceTax(), new BigDecimal("160.02"));
        assertEquals(resultE.getTaxFreeAllowance(), new BigDecimal("46.33"));
        assertEquals(resultE.getAdvanceTaxPaid(), new BigDecimal("36.19"));
        assertEquals(resultE.getRoundedAdvanceTaxPaid(), new BigDecimal("36"));
        assertEquals(resultE.getNetIncome(), new BigDecimal("736.90"));

        TaxCalculationResult resultC = service.calculate(ContractType.CIVIL, new BigDecimal("1000.00"));
        assertEquals(resultC.getGrossIncome(), new BigDecimal("1000.00"));
        assertEquals(resultC.getTaxDeductibleExpenses(), new BigDecimal("200.00"));
        assertEquals(resultC.getTaxableBase(), new BigDecimal("800.00"));
        assertEquals(resultC.getRoundedTaxableBase(), new BigDecimal("800"));
        assertEquals(resultC.getAdvanceTax(), new BigDecimal("144.00"));
        assertEquals(resultC.getTaxFreeAllowance(), new BigDecimal("0.00"));
        assertEquals(resultC.getAdvanceTaxPaid(), new BigDecimal("66.50"));
        assertEquals(resultC.getRoundedAdvanceTaxPaid(), new BigDecimal("66"));
        assertEquals(resultC.getNetIncome(), new BigDecimal("706.90"));
    }

    private static void testInvalidContractThrowsException() {
        try {
            ContractType.fromCode('Z');
            throw new AssertionError("Expected IllegalArgumentException for unknown contract code 'Z'");
        } catch (IllegalArgumentException expected) {
        }
    }

    private static void assertEquals(BigDecimal actual, BigDecimal expected) {
        if (actual.compareTo(expected) != 0) {
            throw new AssertionError("Expected: " + expected + ", but got: " + actual);
        }
    }

    private static void testEmploymentContractWith1000() {
        String output = runWithInput("1000\nE\n");
        assertContains(output, "EMPLOYMENT");
        assertContains(output, "Income 1000");
        assertContains(output, "Social security tax 97");
        assertContains(output, "Health social security tax    15");
        assertContains(output, "Sickness social security tax  24");
        assertContains(output, "Tax deductible expenses 111.25");
        assertContains(output, "income 888.75 rounded 889");
        assertContains(output, "Advance tax 18 % = 160.02");
        assertContains(output, "Tax free income = 46.33");
        assertContains(output, "Reduced tax = 113");
        assertContains(output, "Advance tax paid = 36");
        assertContains(output, "Net income = 736");
    }

    private static void testEmploymentContractWith5000() {
        String output = runWithInput("5000\nE\n");
        assertContains(output, "EMPLOYMENT");
        assertContains(output, "Income 5000");
        assertContains(output, "Social security tax 488");
        assertContains(output, "Tax deductible expenses 111.25");
        assertContains(output, "income 4888.75 rounded 4889");
        assertContains(output, "Advance tax 18 % = 880.02");
        assertContains(output, "Reduced tax = 833");
        assertContains(output, "Advance tax paid = 446");
        assertContains(output, "Net income = 3418");
    }

    private static void testCivilContractWith1000() {
        String output = runWithInput("1000\nC\n");
        assertContains(output, "income 1000");
        assertContains(output, "Social security tax = 97");
        assertContains(output, "Tax deductible expenses = 200");
        assertContains(output, "income to be taxed = 800");
        assertContains(output, "Advance tax 18 % = 144");
        assertContains(output, "Already paid tax = 144");
        assertContains(output, "Advance tax  = 66");
        assertContains(output, "Net income = 706");
    }

    private static void testCivilContractWith5000() {
        String output = runWithInput("5000\nC\n");
        assertContains(output, "income 5000");
        assertContains(output, "Social security tax = 488");
        assertContains(output, "Tax deductible expenses = 1000");
        assertContains(output, "income to be taxed = 4000");
        assertContains(output, "Advance tax 18 % = 720");
        assertContains(output, "Already paid tax = 720");
        assertContains(output, "Advance tax  = 332");
        assertContains(output, "Net income = 3532");
    }

    private static String runWithInput(String input) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setIn(in);
            System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

            TaxCalculator.main(new String[0]);

            return out.toString(StandardCharsets.UTF_8);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    private static void assertContains(String text, String expectedSnippet) {
        if (!text.contains(expectedSnippet)) {
            throw new AssertionError("Expected text to contain snippet: '" + expectedSnippet + "', but received:\n" + text);
        }
    }
}
