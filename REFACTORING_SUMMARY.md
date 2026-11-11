# Refactoring the Tax Calculator: What We Changed and Why

This document tells the story of how the tax calculator was refactored. Each decision is described in the same way:

- **What was wrong**: the problem we found.
- **Why it mattered**: why we saw it as a problem.
- **What we changed**: the fix, with an example and the summary in short.

Unless stated otherwise, the program output shown is for a monthly income of 1000.00 PLN.

<a id="contents"></a>

## Contents

- [Refactoring the Tax Calculator: What We Changed and Why](#refactoring-the-tax-calculator-what-we-changed-and-why)
  - [Contents](#contents)
  - [Where we started](#where-we-started)
  - [Iteration 1](#iteration-1)
    - [1.1 Make it compile, then lock its behavior with tests](#11-make-it-compile-then-lock-its-behavior-with-tests)
    - [1.2 Give every number a name](#12-give-every-number-a-name)
    - [1.3 Give variables meaningful names](#13-give-variables-meaningful-names)
    - [1.4 Replace global variables with one result object](#14-replace-global-variables-with-one-result-object)
    - [1.5 Replace the letters 'E' and 'C' with a proper type](#15-replace-the-letters-e-and-c-with-a-proper-type)
    - [1.6 Remove the copy-paste between contract types](#16-remove-the-copy-paste-between-contract-types)
    - [1.7 Split the giant `main` method](#17-split-the-giant-main-method)
    - [1.8 Delete dead code and comments](#18-delete-dead-code-and-comments)
    - [1.9 Use exact numbers for money, and less boilerplate](#19-use-exact-numbers-for-money-and-less-boilerplate)
    - [1.10 Organize the files into packages](#110-organize-the-files-into-packages)
  - [Iteration 2](#iteration-2)
    - [2.1 A proper build with Maven](#21-a-proper-build-with-maven)
    - [2.2 Real, readable tests](#22-real-readable-tests)
    - [2.3 Hand the strategies to the service from outside](#23-hand-the-strategies-to-the-service-from-outside)
    - [2.4 One report printer per contract type](#24-one-report-printer-per-contract-type)
  - [Iteration 3](#iteration-3)
    - [3.1 Reading input through an interface](#31-reading-input-through-an-interface)
    - [3.2 Separating *what* the report says from *where* it goes](#32-separating-what-the-report-says-from-where-it-goes)
    - [3.3 Separating the application flow from `main`](#33-separating-the-application-flow-from-main)
  - [Iteration 4: The report drives the output](#iteration-4-the-report-drives-the-output)
    - [4.1 Each calculation produces one structured report](#41-each-calculation-produces-one-structured-report)
  - [Iteration 5: Strategies built from parts](#iteration-5-strategies-built-from-parts)
    - [5.1 Build each contract type from small parts instead of a parent class](#51-build-each-contract-type-from-small-parts-instead-of-a-parent-class)
  - [Correctness fixes (outside the refactoring)](#correctness-fixes-outside-the-refactoring)
    - [F.1 Tax calculation follows Polish tax rules](#f1-tax-calculation-follows-polish-tax-rules)
    - [F.2 Two wrong lines in the report](#f2-two-wrong-lines-in-the-report)
  - [The result](#the-result)

<a id="where-we-started"></a>

## Where we started

The program asks for a monthly income and a contract type (employment or civil contract). It then calculates Polish social security contributions, the health contribution, the advance income tax and the net income.

The original code was a single file, `TaxCalculator.java`, and it had serious problems:

- **It did not compile.** Careless find-and-replace had produced broken names such as `advanceTaxPaidadvanceTax`.
- **Everything happened in one huge `main` method** of over 100 lines. It read the input, did the math and printed the result.
- **The two contract types were copy-pasted.** Most of the employment and civil contract code was the same.
- **Numbers had no names.** Values such as `9.76`, `7.75` or `111.25` appeared with no explanation.
- **Names were cryptic**, for example `br`, `df00`, `soc_health1` or `taxedIncome0`.
- **All data lived in 12 global variables** that any part of the code could change at any time.
- **Money was stored as `double`**, a number type that cannot represent amounts such as 0.10 exactly.

The refactoring took five iterations. After each one we reviewed the code again and agreed what to do next.

<sub>[↑ Back to contents](#contents)</sub>

---

<a id="iteration-1"></a>

## Iteration 1

<a id="s1-1"></a>

### 1.1 Make it compile, then lock its behavior with tests

**What was wrong:** The code did not compile, and nothing checked what it produced.

**Why it mattered:** Refactoring means changing the structure without changing the results. That is only possible if we can prove that the results stay the same.

**What we changed:** We fixed the compile errors with the smallest possible edits. Then we wrote a test file that records the exact results for sample incomes, and every later change had to keep those tests passing.

**Example: the compile errors and their fixes.** The compiler reported 13 errors. A 14th error appeared once those were fixed. All of them came from 7 broken lines in `TaxCalculator.java`:

| # | Before (broken) | After (fixed) | Problem |
| --- | --- | --- | --- |
| 1 | `public static double advanceTaxPaidadvanceTax = 0; // advance tax 18%` | `public static double advanceTax = 0;` | Garbled name. 6 places used `advanceTax`, which did not exist |
| 2 | `public static double advanceTaxPaidadvanceTax = 0;` (second time) | `public static double advanceTaxPaid = 0;` | Same name declared twice. 4 places used `advanceTaxPaid`, which did not exist |
| 3 | `advanceTaxPaidadvanceTax = advanceTax - soc_health2 - taxFreeIncome;` | `advanceTaxPaid = advanceTax - soc_health2 - taxFreeIncome;` | Wrote to the garbled name |
| 4 | `soc_sick_secur = (income * 2.45) / 100;` | `soc_sick_security = (income * 2.45) / 100;` | Name cut short |
| 5 | `return (income - ... - soc_sick_secur);` | `return (income - ... - soc_sick_security);` | Name cut short |
| 6 | `double  advanceTax;` (civil branch) | `double taxPaid = advanceTax;` | The next line printed `taxPaid`, which did not exist in this branch |
| 7 | `double income = calculateIncome(income);` (civil branch) | `double d_income = calculateIncome(income);` | A new variable `income` hid the real one and was used before it had a value |

With these 7 fixes the program runs. For 1000 PLN it prints a net income of 736.90 (employment) and 706.90 (civil contract), and these are the values the tests then locked in.

**Example: the first tests.** We added a new file, `TaxCalculatorTest.java`, next to `TaxCalculator.java`. At that stage there was no test framework, so it was a plain class with its own `main` method that you ran by hand. It called the calculator with sample inputs and stopped with an error if any value differed:

```java
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
        TaxCalculationResult resultE = service.calculate(ContractType.EMPLOYMENT, 1000.0);
        assertEquals(resultE.getPensionContribution(), 97.60);
        assertEquals(resultE.getAdvanceTax(), 160.02);
        assertEquals(resultE.getNetIncome(), 736.90);
        ...
    }

    private static void testCivilContractWith1000() {
        String output = runWithInput("1000\nC\n");   // types "1000" and "C" as if by keyboard
        assertContains(output, "Tax deductible expenses = 200.0");
        assertContains(output, "Net income = 706");
        ...
    }
}
```

**All changes:**

- The 7 line fixes in the table above.
- New file `TaxCalculatorTest.java` with 6 checks:
  - all calculated values for employment and civil contracts at 1000 PLN;
  - the printed report for employment at 1000 and 5000 PLN;
  - the printed report for civil contracts at 1000 and 5000 PLN;
  - that an unknown contract letter (`Z`) is rejected.

<sub>[↑ Back to contents](#contents)</sub>

<a id="s1-2"></a>

### 1.2 Give every number a name

**What was wrong:** Tax rates were scattered through the formulas as bare numbers, and many were repeated.

**Why it mattered:** A reader cannot tell that `7.75` is the deductible part of the health contribution. When a rate changes, someone has to find every copy of it.

**What we changed:** All rates and fixed amounts now live in one new class, `TaxRates`, and each has a descriptive name.

**Example.** In `TaxCalculator.java`, before:

```java
soc_security = (income * 9.76) / 100;
```

In `TaxRates.java` and the calculation, after:

```java
public static final BigDecimal PENSION_CONTRIBUTION_RATE = new BigDecimal("0.0976");
...
BigDecimal pensionContribution = applyRate(grossIncome, TaxRates.PENSION_CONTRIBUTION_RATE);
```

**All changes:** every number below was replaced by a named constant in `TaxRates`.

| Number in old code | Name now |
| --- | --- |
| `9.76` | `PENSION_CONTRIBUTION_RATE` |
| `1.5` | `DISABILITY_CONTRIBUTION_RATE` |
| `2.45` | `SICKNESS_CONTRIBUTION_RATE` |
| `9` | `HEALTH_CONTRIBUTION_RATE` |
| `7.75` | `TAX_DEDUCTIBLE_HEALTH_RATE` |
| `18` | `ADVANCE_TAX_RATE` |
| `111.25` | `STANDARD_EMPLOYMENT_EXPENSES` |
| `46.33` | `EMPLOYMENT_TAX_FREE_ALLOWANCE` |
| `20` (civil expenses) | `CIVIL_EXPENSES_PERCENTAGE` |
| `0` (civil allowance, `taxFreeIncome = 0`) | `CIVIL_TAX_FREE_ALLOWANCE` |

<sub>[↑ Back to contents](#contents)</sub>

<a id="s1-3"></a>

### 1.3 Give variables meaningful names

**What was wrong:** Many names were abbreviations or numbered copies, and some were simply wrong.

**Why it mattered:** A reader had to work out each formula to understand what a variable held. The name `soc_health_security` even pointed at the wrong thing: it held the disability contribution, not a health contribution.

**What we changed:** Every value now has a name that says what it is in tax terms.

**Example.** Before:

```java
soc_health1 = (income * 9) / 100;
soc_health2 = (income * 7.75) / 100;
```

After:

```java
BigDecimal healthContribution = applyRate(..., TaxRates.HEALTH_CONTRIBUTION_RATE);
BigDecimal taxDeductibleHealthContribution = applyRate(..., TaxRates.TAX_DEDUCTIBLE_HEALTH_RATE);
```

**All changes:**

| Old name | New name |
| --- | --- |
| `income` | `grossIncome` |
| `soc_security` | `pensionContribution` |
| `soc_health_security` | `disabilityContribution` (the old name was wrong) |
| `soc_sick_security` | `sicknessContribution` |
| `soc_health1` | `healthContribution` |
| `soc_health2` | `taxDeductibleHealthContribution` |
| `taxFreeIncome` | `taxFreeAllowance` |
| `taxedIncome` / `taxedIncome0` | `taxableBase` / `roundedTaxableBase` |
| `taxPaid` | `reducedTax` |
| `advanceTaxPaid0` | `roundedAdvanceTaxPaid` |
| `isr`, `br` | `reader` |
| `df00`, `df` | `currencyFormat`, `integerFormat` |

<sub>[↑ Back to contents](#contents)</sub>

<a id="s1-4"></a>

### 1.4 Replace global variables with one result object

**What was wrong:** All values lived in 12 `public static` variables. Helper methods returned nothing; they quietly changed these globals, and `main` read them afterwards.

**Why it mattered:** You could not tell what a method did without reading its body. Any code could change any value at any time, and two calculations could not run side by side.

**What we changed:** The calculation now works with local variables and returns all its values together in one object, `TaxCalculationResult`, which cannot be changed after it is created.

**Example.** Before, a method silently wrote to a global variable:

```java
public static double advanceTax = 0;
...
public static void calculateTax(double income) {
    advanceTax = (income * 18) / 100;
}
```

After, values are computed locally and handed back as one result:

```java
BigDecimal advanceTax = applyRate(roundedTaxableBase, TaxRates.ADVANCE_TAX_RATE);
...
return TaxCalculationResult.builder()
        .advanceTax(advanceTax)
        ...
        .build();
```

**All changes:**

- New class `TaxCalculationResult`, which holds the contract type and all 17 calculated values.
- The 4 helper methods that changed globals (`calculateIncome`, `calculateOtherTaxes`, `calculateTax`, `calculateAdvanceTax`) were replaced by one `calculate(...)` method that returns a result.
- The input (income and contract type) is now held in a small object, `InputData`, instead of the globals `income` and `contractType`.

<sub>[↑ Back to contents](#contents)</sub>

<a id="s1-5"></a>

### 1.5 Replace the letters 'E' and 'C' with a proper type

**What was wrong:** The contract type was a single character, checked with `if (contractType == 'E')`.

**Why it mattered:** A character can hold any letter, so invalid values could slip through, and the checks were repeated across the code.

**What we changed:** We added a `ContractType` *enum*, a fixed list of allowed values: `EMPLOYMENT` and `CIVIL`. The typed letter is converted to this type once, right after input, and an unknown letter is rejected immediately.

**Example.** Before, in `main`:

```java
contractType = br.readLine().charAt(0);
if (contractType == 'E') { ... }
else if (contractType == 'C') { ... }
else { System.out.println("Unknown type of contract!"); }
```

After, in the new file `ContractType.java`:

```java
public enum ContractType {
    EMPLOYMENT('E', "EMPLOYMENT"),
    CIVIL('C', "CIVIL");
    ...
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
```

**All changes:**

- New enum `ContractType`, where each value knows its letter (`E`, `C`) and its display name.
- Lowercase `e` and `c` are now accepted too.
- An unknown letter now ends with the program's standard `Incorrect` message instead of a separate `Unknown type of contract!` branch.
- Every `char` comparison in the code was replaced by `ContractType` values.

<sub>[↑ Back to contents](#contents)</sub>

<a id="s1-6"></a>

### 1.6 Remove the copy-paste between contract types

**What was wrong:** The employment and civil contract calculations were two nearly identical blocks of code.

**Why it mattered:** A fix in one block could easily be missed in the other. Adding a third contract type would have meant pasting another 40 lines into a growing `if/else` chain.

**What we changed:** The shared calculation steps now exist once, in a base class. Each contract type is a small class that defines only what is different about it (*Strategy pattern*). A service picks the right strategy for the chosen contract type.

**Example.** Before, the same lines appeared in both branches of `main`:

```java
if (contractType == 'E') {
    ...
    double taxedIncome = income - taxDeductibleExpenses ;
    double taxedIncome0 = Double.parseDouble(df.format(taxedIncome));
    calculateTax(taxedIncome0);
    ...
} else if (contractType == 'C') {
    ...
    double taxedIncome = income - taxDeductibleExpenses ;
    double taxedIncome0 = Double.parseDouble(df.format(taxedIncome));
    calculateTax(taxedIncome0);
    ...
}
```

After, the steps are written once in `AbstractTaxStrategy`, and the employment strategy contains only what makes it different:

```java
public class EmploymentTaxStrategy extends AbstractTaxStrategy {

    @Override
    protected BigDecimal calculateTaxDeductibleExpenses(BigDecimal grossIncome) {
        return TaxRates.STANDARD_EMPLOYMENT_EXPENSES;
    }

    @Override
    protected BigDecimal getTaxFreeAllowance() {
        return TaxRates.EMPLOYMENT_TAX_FREE_ALLOWANCE;
    }
}
```

**All changes:**

- New interface `TaxStrategy`: "given a gross income, calculate the result".
- New class `AbstractTaxStrategy`, which contains every shared step once: social security, health contribution, taxable base, advance tax and net income.
- New class `EmploymentTaxStrategy`: fixed expenses of 111.25 PLN and an allowance of 46.33 PLN.
- New class `CivilContractTaxStrategy`: expenses of 20% of income and no allowance.
- New class `TaxCalculatorService`, which picks the strategy for the contract type and rejects a negative income.

<sub>[↑ Back to contents](#contents)</sub>

<a id="s1-7"></a>

### 1.7 Split the giant `main` method

**What was wrong:** One method read the input, calculated, formatted and printed.

**Why it mattered:** A method that does everything is hard to read and hard to test. A small change to the printed text meant touching the same code as the tax math. The Clean Code rule here is that each class should have *one reason to change*.

**What we changed:** Reading, calculating and printing became separate pieces. `main` now only connects them.

**Example.** Before, `main` was over 100 lines of input reading, number formats, math and printing:

```java
public static void main(String[] args) {
    try {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        ...
    }
    DecimalFormat df00 = new DecimalFormat("#.00");
    DecimalFormat df = new DecimalFormat("#");
    if (contractType == 'E') {
        System.out.println("EMPLOYMENT");
        ... // about 90 more lines of math and printing
    }
}
```

After, `main` is a short list of steps, each handled elsewhere:

```java
public static void main(String[] args) {
    try {
        InputData input = readInput();

        TaxCalculatorService service = new TaxCalculatorService();
        TaxCalculationResult result = service.calculate(input.getContractType(), input.getIncome());

        TaxReportPrinter printer = new TaxReportPrinter();
        printer.printReport(result);
    } catch (Exception ex) {
        System.out.println("Incorrect");
        System.err.println(ex);
    }
}
```

**All changes:**

- Input reading moved to a separate method, `readInput()`.
- The math moved to the strategies (see [1.6](#s1-6)).
- All printing moved to a new class, `TaxReportPrinter`.
- The printed text was kept exactly as before, so the tests from [1.1](#s1-1) still passed.

<sub>[↑ Back to contents](#contents)</sub>

<a id="s1-8"></a>

### 1.8 Delete dead code and comments

**What was wrong:** During the first restructuring we had kept the old global variables and methods, marked as deprecated, "just in case". The code also had many comments that only repeated what the code already said.

**Why it mattered:** Unused code confuses readers and goes stale. Comments that repeat the code drift out of date and start to mislead. Git already keeps the history, so there is no need to keep old code around.

**What we changed:** We deleted all the legacy code and all the comments. Clear names now explain the code instead.

**Example.** This block in `TaxCalculator.java` was deleted completely. It only copied the new results back into old globals that nothing read any more:

```java
@Deprecated public static double soc_security = 0;
@Deprecated public static double soc_health1 = 0;
...
private static void populateLegacyFields(TaxCalculationResult result) {
    soc_security = result.getPensionContribution();
    soc_health1 = result.getHealthContribution();
    ...
}
```

A comment that existed only because of a bad name. Before:

```java
public static double soc_security = 0; // 9,76% of basis
```

After, the name says it all:

```java
public static final BigDecimal PENSION_CONTRIBUTION_RATE = new BigDecimal("0.0976");
```

**All changes:**

- Deleted 12 deprecated global variables.
- Deleted the `populateLegacyFields` method.
- Deleted the 4 deprecated legacy methods: `calculateAdvanceTax`, `calculateTax`, `calculateIncome` and `calculateOtherTaxes`.
- Removed all comments from all 11 files:
  - the class descriptions (Javadoc);
  - the step markers in the calculation (`// 1. Social security contributions`);
  - the end-of-line notes next to each rate in `TaxRates`;
  - the `// Success` note in a test.

<sub>[↑ Back to contents](#contents)</sub>

<a id="s1-9"></a>

### 1.9 Use exact numbers for money, and less boilerplate

**What was wrong:** Money was calculated with `double`. The data classes were also full of hand-written constructors and getters.

**Why it mattered:** `double` cannot store most decimal amounts exactly, so small errors appear, which is unacceptable for tax. The boilerplate buried the few lines that actually mattered.

**What we changed:** All money now uses `BigDecimal`, which stores decimal values exactly and rounds only when and how we say. *Lombok* writes the constructors and getters for us.

**Example: exact numbers.** With `double`, the pension contribution for an income of 1000.10 PLN is:

```java
1000.10 * 9.76 / 100   // = 97.60976000000001
```

The old code also rounded by turning a number into text and parsing it back:

```java
double taxedIncome0 = Double.parseDouble(df.format(taxedIncome));
```

After, rounding is one explicit rule in one place:

```java
protected BigDecimal roundToWhole(BigDecimal amount) {
    return amount.setScale(INTEGER_SCALE, ROUNDING_MODE);
}
```

**Example: less boilerplate.** Before, `TaxCalculationResult` listed each of its 18 values four times: as a field, a constructor parameter, an assignment and a getter. The class was about 150 lines long:

```java
private final double grossIncome;
...
public TaxCalculationResult(ContractType contractType, double grossIncome, double pensionContribution, ...) {
    this.contractType = contractType;
    this.grossIncome = grossIncome;
    ...
}
public double getGrossIncome() { return grossIncome; }
...
```

After, three annotations replace all of it:

```java
@Getter
@Builder
@AllArgsConstructor
public class TaxCalculationResult {
    private final ContractType contractType;
    private final BigDecimal grossIncome;
    ...
}
```

**All changes:**

- `double` became `BigDecimal` everywhere money is handled:
  - all the rates in `TaxRates`, now written as exact text such as `new BigDecimal("0.0976")`;
  - every step in `AbstractTaxStrategy`;
  - the `TaxStrategy` and `TaxCalculatorService` methods;
  - `TaxCalculationResult`;
  - the input.
- The income is read with `new BigDecimal(...)`, and the input is trimmed so stray spaces no longer break it.
- Text-based rounding was replaced by two helpers: `applyRate` (to the grosz) and `roundToWhole` (to the złoty).
- The service now also rejects a missing (`null`) income.
- Lombok annotations replaced the hand-written code in:
  - `TaxCalculationResult` (`@Getter`, `@Builder`, `@AllArgsConstructor`);
  - `ContractType` (`@Getter`, `@AllArgsConstructor`);
  - `InputData` (`@Getter`, `@AllArgsConstructor`).
- The tests were updated to compare `BigDecimal` values.

<sub>[↑ Back to contents](#contents)</sub>

<a id="s1-10"></a>

### 1.10 Organize the files into packages

**What was wrong:** All classes sat in one flat folder.

**Why it mattered:** As the number of files grew, it became hard to see which classes do the math, which print and which only hold data.

**What we changed:** We grouped the classes into folders by their role.

**Example.** Before, all 11 files sat in the same folder. After, each class moved to a folder that matches its role:

| File | Folder before | Folder after | Role |
| --- | --- | --- | --- |
| `ContractType.java` | (top level) | `model/` | Data |
| `TaxCalculationResult.java` | (top level) | `model/` | Data |
| `TaxRates.java` | (top level) | `model/` | Data |
| `TaxStrategy.java` | (top level) | `strategy/` | Tax math |
| `AbstractTaxStrategy.java` | (top level) | `strategy/` | Tax math |
| `EmploymentTaxStrategy.java` | (top level) | `strategy/` | Tax math |
| `CivilContractTaxStrategy.java` | (top level) | `strategy/` | Tax math |
| `TaxCalculatorService.java` | (top level) | `service/` | Picks the strategy |
| `TaxReportPrinter.java` | (top level) | `view/` | Printing |
| `TaxCalculator.java` | (top level) | (top level) | Program entry point |
| `TaxCalculatorTest.java` | (top level) | (top level) | Tests |

**All changes:**

- 9 classes moved into the `model`, `strategy`, `service` and `view` folders, with matching package names and imports.
- As a temporary measure, so that the project could be run on a team member's laptop, we added the Lombok library (`lib/lombok.jar`) and IntelliJ project files (`.idea/`, `SEM.iml`).

<sub>[↑ Back to contents](#contents)</sub>

<a id="iteration-2"></a>

## Iteration 2

<a id="s2-1"></a>

### 2.1 A proper build with Maven

**What was wrong:** The project had no build tool, neither originally nor after iteration 1. Once the code used a library (Lombok) and was split into folders, compiling it needed a long hand-typed command. Our temporary workaround from [1.10](#s1-10), a library file stored in Git plus IntelliJ settings, made it run on one laptop only. The folders also did not match the Java package names, so only IntelliJ could understand the project.

**Why it mattered:** Anyone else who wanted to build the project had to reproduce that setup by hand. Clean Code says building and testing should take a single step.

**What we changed:** We added Maven, the standard Java build tool, and removed the temporary workaround. `mvn test` now builds and tests everything.

**Example.** Before, building needed this command, plus the `lib/lombok.jar` we had added:

```bash
javac -cp "lib/lombok.jar" -processorpath "lib/lombok.jar" -d . model/*.java strategy/*.java service/*.java view/*.java TaxCalculator.java TaxCalculatorTest.java
```

After, libraries are declared in `pom.xml`, Maven downloads them, and one command does everything:

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>${lombok.version}</version>
    <scope>provided</scope>
</dependency>
```

```bash
mvn test
```

**All changes:**

- New `pom.xml` with:
  - Java 17;
  - the libraries: Lombok, JUnit 5 and AssertJ;
  - fixed versions for every build plugin.
- Code moved to `src/main/java/com/bartoszwalter/students/taxes/...`, so the folders now match the package names.
- Tests moved to `src/test/java/...`, separate from the program code.
- Deleted from Git the temporary files we had added in [1.10](#s1-10):
  - `lib/lombok.jar` (2 MB);
  - the `.idea/` folder;
  - `SEM.iml`.
- `.gitignore` now ignores `target/`, `.idea/`, `*.iml` and `out/`.
- The files were moved, not copied, so Git keeps each file's history.

<sub>[↑ Back to contents](#contents)</sub>

<a id="s2-2"></a>

### 2.2 Real, readable tests

**What was wrong:** The test file from [1.1](#s1-1) had several weaknesses:

- It was a hand-run `main` method, and the first failure stopped the whole run.
- Its checks were too loose: a check for `97.60` also passed for `97.99`.
- Important cases had no tests:
  - zero income;
  - negative income;
  - rounding at exactly 0.50;
  - lowercase contract letters.

**Why it mattered:** Weak tests give a false sense of safety.

**What we changed:** We deleted the old file and switched to JUnit 5, the standard Java test framework, with AssertJ for readable checks. There is now one test file per program class, in `src/test/java`, mirroring the program's folders. Each test checks one thing, has a name that describes the expected behavior and compares exact values.

**Example: the same check, old and new.** Checking that an unknown contract letter is rejected. Before, in the old `TaxCalculatorTest.java`, with an empty `catch` that hides what the test means:

```java
private static void testInvalidContractThrowsException() {
    try {
        ContractType.fromCode('Z');
        throw new AssertionError("Expected IllegalArgumentException for unknown contract code 'Z'");
    } catch (IllegalArgumentException expected) {
    }
}
```

After, in the new file `src/test/java/.../model/ContractTypeTest.java`:

```java
@Test
void rejectsUnknownCode() {
    assertThatThrownBy(() -> ContractType.fromCode('Z'))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Z");
}

@ParameterizedTest
@CsvSource({"E, EMPLOYMENT", "e, EMPLOYMENT", "C, CIVIL", "c, CIVIL"})
void resolvesContractTypeFromCodeIgnoringCase(char code, ContractType expectedType) {
    assertThat(ContractType.fromCode(code)).isEqualTo(expectedType);
}
```

**Example: loose vs exact checks.** Before, only the start of a printed line was checked:

```java
assertContains(output, "Social security tax 97");   // passes for 97.60, 97,60 and 97.99
```

After, in `strategy/EmploymentTaxStrategyTest.java`, the exact value is checked:

```java
assertThat(result.getPensionContribution()).isEqualByComparingTo("97.60");
```

**All changes:**

- The old hand-run `TaxCalculatorTest.java` was deleted.
- JUnit 5 and AssertJ were added, and the first JUnit test, `ContractTypeTest`, was written.
- From here on, every decision adds tests for the code it changes. They are listed under "All changes" in each decision.

Today the suite has **75 tests in 15 files**, and `mvn test` runs all of them:

- **11 unit test files**, one per class, each checking that class on its own;
- **2 contract test files**, which check every calculated value for employment and civil contracts, including rounding and low incomes;
- **1 flow test**, which runs read → calculate → report with in-memory input and output;
- **1 end-to-end test**, which runs the real program, including invalid input.

<sub>[↑ Back to contents](#contents)</sub>

<a id="s2-3"></a>

### 2.3 Hand the strategies to the service from outside

**What was wrong:** The calculator service created the employment and civil strategies itself. It also had a public method that let anyone add strategies later.

**Why it mattered:** Adding a contract type still meant editing the service, so the promise of "just add a new class" from [1.6](#s1-6) was not quite true. The public method also meant the service could be changed while the program was running.

**What we changed:** The strategies are now passed into the service when it is created, all in one place next to `main`. A small reusable lookup, `ContractTypeRegistry`, finds the right one for a contract type.

**Example.** Before, in `TaxCalculatorService.java`:

```java
public TaxCalculatorService() {
    registerStrategy(new EmploymentTaxStrategy());
    registerStrategy(new CivilContractTaxStrategy());
}

public void registerStrategy(TaxStrategy strategy) {
    strategies.put(strategy.getContractType(), strategy);
}
```

After, the service just receives its strategies:

```java
public TaxCalculatorService(Collection<? extends TaxStrategy> strategies) {
    this.strategies = new ContractTypeRegistry<>(strategies);
}
```

The strategies are chosen in `TaxCalculator.java`:

```java
private static TaxCalculatorService createCalculatorService() {
    return new TaxCalculatorService(List.of(
            new EmploymentTaxStrategy(),
            new CivilContractTaxStrategy()));
}
```

**All changes:**

- New interface `ContractTypeAware`: "I belong to contract type X".
- New class `ContractTypeRegistry`:
  - it maps each contract type to its implementation;
  - it refuses two implementations for the same type;
  - it gives a clear error for a type that has no implementation.
- `TaxStrategy` now extends `ContractTypeAware`.
- `TaxCalculatorService`:
  - its constructor takes the strategies;
  - the public `registerStrategy` method and the service's own lookup code were removed.
- New method `createCalculatorService()` in `TaxCalculator.java`.
- New tests: `ContractTypeRegistryTest`, `TaxCalculatorServiceTest`.

<sub>[↑ Back to contents](#contents)</sub>

<a id="s2-4"></a>

### 2.4 One report printer per contract type

**What was wrong:** The report printer still branched with `if employment … else if civil …`, the same kind of branching we had just removed from the calculations. It also had other problems:

- On a computer set to Polish, amounts printed with a comma (`97,60`).
- Some amounts were not formatted at all (`Income 1000`).
- Percentages were typed into the labels by hand, duplicating `TaxRates`.

**Why it mattered:** The output was inconsistent, and every new contract type would have meant editing the printer.

**What we changed:** Each contract type got its own small printer, and all number formatting moved to one place.

**Example.** Before, in `TaxReportPrinter.java`, the last branch could never run:

```java
public void printReport(TaxCalculationResult result) {
    if (result.getContractType() == ContractType.EMPLOYMENT) {
        printEmploymentReport(result);
    } else if (result.getContractType() == ContractType.CIVIL) {
        printCivilReport(result);
    } else {
        out.println("Unknown type of contract!");
    }
}
```

Program output before (computer set to Polish) and after:

| Before | After |
| --- | --- |
| `Income 1000` | `Income 1000.00` |
| `Social security tax 97,60` | `Social security tax 97.60` |
| `Income basis for health social security: 1000` | `Income basis for health social security: 1000.00` |
| `Health social security tax: 9% = 90,00 7,75% = 77,50` | `Health social security tax: 9% = 90.00 7.75% = 77.50` |

**All changes:**

- `TaxReportPrinter` changed from a class to an interface.
- New classes `EmploymentReportPrinter` and `CivilContractReportPrinter`, one per contract type.
- New class `ReportValueFormatter`, which replaces the old number formats (`DecimalFormat`):
  - amounts always show 2 decimals and a dot (`97.60`);
  - rounded amounts show none (`752`);
  - rates show as percentages (`18%`).
- Percentages in the labels now come from `TaxRates` instead of being typed in.
- Extra spaces in labels were removed. For example, `"Advance tax  = "` became `"Advance tax = "`.
- The printers are chosen through `ContractTypeRegistry` in `main`, and the unreachable `Unknown type of contract!` branch is gone.
- New tests: `EmploymentReportPrinterTest`, `CivilContractReportPrinterTest`, `ReportValueFormatterTest`.
- Two wrong lines in the report were also corrected at this point. They are described separately in [F.2](#f2), because they change what the report says.

<sub>[↑ Back to contents](#contents)</sub>

> **Note:** At the end of iteration 2, the tax calculation itself was corrected to follow Polish tax rules. That is a bug fix, not a refactoring, so it is described separately in [F.1](#f1). From this point on, the examples show the corrected numbers.

---

<a id="iteration-3"></a>

## Iteration 3

Today the program reads from the keyboard and prints to the console, but it should be possible to switch to a file, a GUI or a web page later without rewriting the core.

<sub>[↑ Back to contents](#contents)</sub>

<a id="s3-1"></a>

### 3.1 Reading input through an interface

**What was wrong:** Reading from the keyboard was written directly into `TaxCalculator.java`.

**Why it mattered:** Getting the income from anywhere else would have meant rewriting `main`.

**What we changed:** We added an interface, `TaxInputReader`, which says only "give me the income and contract type". The keyboard version is one implementation of it.

**Example.** Before, in `TaxCalculator.java`:

```java
private static InputData readInput() throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.print("Enter income: ");
    BigDecimal incomeInput = new BigDecimal(reader.readLine().trim());
    ...
}
```

After, the program depends only on this interface (new file `input/TaxInputReader.java`):

```java
public interface TaxInputReader {

    TaxInput read();
}
```

The keyboard code moved into `input/ConsoleTaxInputReader.java`, which implements this interface.

**All changes:**

- New interface `TaxInputReader`.
- New class `ConsoleTaxInputReader`, which holds the keyboard code moved out of `TaxCalculator`. It receives the input and the prompt output from outside instead of using `System.in` directly, so tests can feed it text.
- The private `InputData` class inside `TaxCalculator` became a public model, `model/TaxInput`.
- Reading errors are wrapped so that the interface itself mentions no keyboard or stream details. This way, a GUI or file reader can implement it as well.
- `readInput()` was removed from `TaxCalculator`.
- New test: `ConsoleTaxInputReaderTest`.

<sub>[↑ Back to contents](#contents)</sub>

<a id="s3-2"></a>

### 3.2 Separating *what* the report says from *where* it goes

**What was wrong:** Each printer decided both the content of the report and that it goes to the console.

**Why it mattered:** Supporting a second destination, such as a file, would have meant duplicating every printer. With 2 contract types and 3 destinations, that is 6 classes, and the number multiplies with every addition.

**What we changed:** We split the printers into two parts. *Report generators* build the content, and *report writers* deliver it somewhere. A new contract type adds one generator and a new destination adds one writer.

**Example.** Before, the printer built and printed each line in the same step:

```java
out.println("EMPLOYMENT");
out.println("Income " + formatAmount(result.getGrossIncome()));
...
```

After, the generator only builds the lines:

```java
public TaxReport generate(TaxCalculationResult result) {
    return new TaxReport(List.of(
            "EMPLOYMENT",
            "Income " + formatAmount(result.getGrossIncome()),
            ...));
}
```

The writer only delivers them, here to the console:

```java
public void write(TaxReport report) {
    report.getLines().forEach(out::println);
}
```

**All changes:**

- Renamed (and changed from printing to returning a report):
  - `TaxReportPrinter` → `TaxReportGenerator`;
  - `EmploymentReportPrinter` → `EmploymentReportGenerator`;
  - `CivilContractReportPrinter` → `CivilContractReportGenerator`.
- New class `TaxReport`, the report's lines. It cannot be changed once created.
- New interface `ReportWriter` and its console version, `ConsoleReportWriter`, in a new `output` folder.
- `main` now combines a generator and the console writer.
- Tests renamed to match. New test: `ConsoleReportWriterTest`.

<sub>[↑ Back to contents](#contents)</sub>

<a id="s3-3"></a>

### 3.3 Separating the application flow from `main`

**What was wrong:** `main` both chose the concrete parts (console, strategies, generators) and ran the steps (read → calculate → report).

**Why it mattered:** Testing the flow required taking over the real keyboard and console.

**What we changed:** A new class, `TaxCalculatorApplication`, runs the steps and knows only the interfaces, not the console. `main` now just assembles the pieces and catches errors.

**Example.** Before, `main` mixed the console with the steps:

```java
public static void main(String[] args) {
    try {
        TaxInput input = new ConsoleTaxInputReader(System.in, System.out).read();
        TaxCalculationResult result = createCalculatorService().calculate(input.getContractType(), input.getIncome());
        new ConsoleReportWriter(System.out).write(createReportGenerators().get(result.getContractType()).generate(result));
    } catch (Exception ex) { ... }
}
```

After, the steps live in `TaxCalculatorApplication.java`, which never mentions the console:

```java
public void run() {
    TaxInput input = inputReader.read();
    TaxCalculationResult result = calculatorService.calculate(input.getContractType(), input.getIncome());
    reportWriter.write(reportGenerators.get(result.getContractType()).generate(result));
}
```

And `main` only plugs in the console parts:

```java
public static void main(String[] args) {
    try {
        createApplication().run();
    } catch (Exception ex) { ... }
}

private static TaxCalculatorApplication createApplication() {
    return new TaxCalculatorApplication(
            new ConsoleTaxInputReader(System.in, System.out),
            createCalculatorService(),
            createReportGenerators(),
            new ConsoleReportWriter(System.out));
}
```

**All changes:**

- New class `TaxCalculatorApplication`. It receives an input reader, the calculator service, the report generators and a report writer, and runs them in order.
- New method `createApplication()` in `TaxCalculator.java`, the only place where the console is chosen.
- Error handling in `main` is unchanged: any failure still prints `Incorrect`.
- New test: `TaxCalculatorApplicationTest`, which runs the whole flow with a ready-made input and collects the reports in a list instead of printing them.

<sub>[↑ Back to contents](#contents)</sub>

---

<a id="iteration-4"></a>

## Iteration 4: The report drives the output

<a id="s4-1"></a>

### 4.1 Each calculation produces one structured report

**What was wrong:** There were still two report generators, one per contract type, and they were almost identical. The report itself was a list of ready-made text lines.

**Why it mattered:** The duplication was back, just in a new place. Ready-made text is also useless for anything that is not plain text. A GUI table or a JSON file needs the actual numbers, and it needs to know whether each number is an amount, a rounded amount or a percentage.

**What we changed:** The calculation result now builds its own report: a title plus a list of entries. Each entry has a label, a value and a kind. One console writer displays any report, so the per-contract generators were deleted.

**Example.** Before, `CivilContractReportGenerator.java` was a near copy of the employment generator, with slightly different wording:

```java
"income " + formatAmount(result.getGrossIncome()),
"Social security tax = " + formatAmount(result.getPensionContribution()),
...
"Already paid tax = " + formatAmount(result.getReducedTax()),
```

After, `TaxCalculationResult.java` describes itself once, for every contract type:

```java
public TaxReport toReport() {
    return new TaxReport(contractType.getDescription(), List.of(
            amount("Gross income", grossIncome),
            amount("Pension contribution", pensionContribution),
            ...
            wholeAmount("Taxable base (rounded)", roundedTaxableBase),
            rate("Advance tax rate", TaxRates.ADVANCE_TAX_RATE),
            ...
            amount("Net income", netIncome)));
}
```

Program output for a civil contract, before and after. The numbers are the same; only the layout and labels changed:

| Before | After |
| --- | --- |
| (no title) | `CIVIL` |
| `income 1000.00` | `Gross income: 1000.00` |
| `Social security tax = 97.60` | `Pension contribution: 97.60` |
| `Disability social security tax = 15.00` | `Disability contribution: 15.00` |
| ... | ... |
| `income to be taxed = 690.32 rounded = 690` | `Taxable base: 690.32`<br>`Taxable base (rounded): 690` |
| `Advance tax 18% = 124.20` | `Advance tax rate: 18%`<br>`Advance tax: 124.20` |
| `Already paid tax = 124.20` | `Tax after allowance: 124.20` |
| `Advance tax = 57.33 rounded = 57` | `Advance tax payable: 57.33`<br>`Advance tax payable (rounded): 57` |
| `Net income = 728.24` | `Net income: 728.24` |

**All changes:**

- New `report` folder with three classes:
  - `ValueFormat`, the kind of value: amount, whole amount or rate;
  - `ReportEntry`, a label, a value and its kind;
  - `TaxReport`, a title and a list of entries.
- New method `TaxCalculationResult.toReport()`, the one description of a report for every contract type.
- `ConsoleReportWriter` now displays any report as `Label: value`, and it alone decides how each kind of value looks.
- `ReportValueFormatter` moved from `view` to `output`, because formatting numbers is part of displaying them.
- Deleted:
  - `TaxReportGenerator`, `EmploymentReportGenerator` and `CivilContractReportGenerator`;
  - the generator registry in `main` and `TaxCalculatorApplication`;
  - the whole `view` folder.
- The flow is now simply `reportWriter.write(result.toReport())`.
- Both contract types share the same, more accurate labels. For example:
  - "Pension contribution" instead of "Social security tax";
  - "Tax after allowance" instead of "Reduced tax" / "Already paid tax".
- The civil report now starts with a `CIVIL` title, as the employment report already did.
- New test: `TaxCalculationResultTest`. Deleted: the two generator tests. `ConsoleReportWriterTest` was rewritten.

<sub>[↑ Back to contents](#contents)</sub>

---

<a id="iteration-5"></a>

## Iteration 5: Strategies built from parts

<a id="s5-1"></a>

### 5.1 Build each contract type from small parts instead of a parent class

**What was wrong:** Since [1.6](#s1-6), the shared calculation lived in a parent class, `AbstractTaxStrategy`. Each contract type was a child class that filled in two gaps: how to calculate the expenses and how big the allowance is.

**Why it mattered:**

- The two contract types differ only in two settings, yet each one needed its own class.
- The settings could not be mixed. A new contract type with fixed expenses (like employment) and no allowance (like civil) could not reuse either class. It would have to copy code into a third one.
- The child classes depended on the inner workings of the parent class. A change in the parent could break every child.

**What we changed:** There is now a single calculation class, `ContractTaxStrategy`, which is given its parts when it is created. The rule for expenses became a small, separate part with two versions, a fixed amount or a percentage of income. Each contract type is now a combination of parts, built in one place, `TaxStrategies`. This is called *composition*: building something out of parts instead of inheriting from a parent. The results did not change at all.

**Example.** Before, the civil contract was a child class of `AbstractTaxStrategy`:

```java
public class CivilContractTaxStrategy extends AbstractTaxStrategy {

    @Override
    public ContractType getContractType() {
        return ContractType.CIVIL;
    }

    @Override
    protected BigDecimal calculateTaxDeductibleExpenses(BigDecimal incomeAfterSocialSecurity) {
        return applyRate(incomeAfterSocialSecurity, TaxRates.CIVIL_EXPENSES_PERCENTAGE);
    }

    @Override
    protected BigDecimal getTaxFreeAllowance() {
        return TaxRates.CIVIL_TAX_FREE_ALLOWANCE;
    }
}
```

After, it is a combination of parts in `TaxStrategies.java`:

```java
public static TaxStrategy civilContract() {
    return new ContractTaxStrategy(
            ContractType.CIVIL,
            new PercentageTaxDeductibleExpenses(TaxRates.CIVIL_EXPENSES_PERCENTAGE),
            TaxRates.CIVIL_TAX_FREE_ALLOWANCE);
}
```

The calculation class receives the parts and uses them. Its steps are the same as before:

```java
public final class ContractTaxStrategy implements TaxStrategy {

    private final ContractType contractType;
    private final TaxDeductibleExpenses taxDeductibleExpenses;
    private final BigDecimal taxFreeAllowance;

    @Override
    public TaxCalculationResult calculate(BigDecimal grossIncome) {
        ...
        BigDecimal deductibleExpenses = taxDeductibleExpenses.calculate(incomeAfterSocialSecurity);
        ...
        BigDecimal reducedTax = nonNegative(advanceTax.subtract(taxFreeAllowance));
        ...
    }
}
```

**All changes:**

- `AbstractTaxStrategy` became `ContractTaxStrategy`. It is no longer a parent class; it is one complete class that receives its parts: a contract type, an expense rule and an allowance. The file was renamed, so Git keeps its history.
- Deleted the child classes `EmploymentTaxStrategy` and `CivilContractTaxStrategy`.
- New interface `TaxDeductibleExpenses`: "given the income after contributions, return the expenses". It has two versions:
  - `FixedTaxDeductibleExpenses`, always the same amount (111.25 PLN for employment);
  - `PercentageTaxDeductibleExpenses`, a percentage of income (20% for civil contracts).
- New class `TaxStrategies` with `employment()` and `civilContract()`, the only place where each contract type is put together.
- The rounding helpers (`applyRate`, `roundToWhole`, `nonNegative`) moved out of the parent class into a new class, `TaxAmounts`, so that any part can use the same rounding rule.
- `main` and the tests now get the strategies from `TaxStrategies`.
- New tests:
  - `ContractTaxStrategyTest`, which checks the calculation with simple test parts;
  - `FixedTaxDeductibleExpensesTest` and `PercentageTaxDeductibleExpensesTest`;
  - `TaxAmountsTest`, which checks rounding at .50 and that negative amounts become zero.
- Unchanged:
  - the expected values in `EmploymentTaxStrategyTest` and `CivilContractTaxStrategyTest`, which still pass;
  - the program output for 100, 300, 1000, 1000.98 and 5000 PLN, which is identical before and after for both contract types.

<sub>[↑ Back to contents](#contents)</sub>

<a id="correctness-fixes"></a>

## Correctness fixes (outside the refactoring)

Refactoring changes how code is built and keeps its results the same. The changes in this section are different: they deliberately changed the results, because the original program calculated or printed something wrong. Each one was made as a separate change, with the tests updated on purpose to the correct values, so that it is never confused with the refactoring around it.

<a id="f1"></a>

### F.1 Tax calculation follows Polish tax rules

*Made at the end of [iteration 2](#iteration-2).*

The refactoring in iteration 1 deliberately kept the original math. That math did not follow Polish tax rules:

| What | Before (wrong) | After (correct) |
| --- | --- | --- |
| Base for the health contribution (9% and 7.75%) | Gross income | Income after social security contributions |
| Base for civil-contract expenses (20%) | Gross income | Income after social security contributions |
| Rounding of tax amounts | "Banker's rounding" (`HALF_EVEN`): 0.50 sometimes rounds down | `HALF_UP`: 0.50 always rounds up, as *Ordynacja podatkowa*, art. 63 requires |
| Taxable base, tax after allowance, tax to pay | Could become negative for low incomes | Never below zero |

**The change in code.** In the shared calculation, before:

```java
RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;
BigDecimal healthContributionBase = grossIncome;
BigDecimal taxableBase = grossIncome.subtract(taxDeductibleExpenses);
```

After:

```java
RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
BigDecimal incomeAfterSocialSecurity = grossIncome.subtract(totalSocialSecurity);
BigDecimal healthContribution = applyRate(incomeAfterSocialSecurity, TaxRates.HEALTH_CONTRIBUTION_RATE);
BigDecimal taxableBase = nonNegative(incomeAfterSocialSecurity.subtract(taxDeductibleExpenses));
```

**The effect on the results**, for an income of 1000.00 PLN:

| Value | Employment before | Employment after | Civil before | Civil after |
| --- | --- | --- | --- | --- |
| Health contribution base | 1000.00 | 862.90 | 1000.00 | 862.90 |
| Health contribution (9%) | 90.00 | 77.66 | 90.00 | 77.66 |
| Tax-deductible expenses | 111.25 | 111.25 | 200.00 | 172.58 |
| Taxable base (rounded) | 889 | 752 | 800 | 690 |
| Tax to pay (rounded) | 36 | 22 | 66 | 57 |
| **Net income** | **736.90** | **763.24** | **706.90** | **728.24** |

For an income of 300.00 PLN on an employment contract, the old code produced a negative tax:

| Before | After |
| --- | --- |
| `Reduced tax = -12.31` | `Reduced tax = 0.00` |
| `Advance tax paid = -35.56 rounded = -36` | `Advance tax paid = 0.00 rounded = 0` |

**Tests:** `EmploymentTaxStrategyTest` and `CivilContractTaxStrategyTest` check the corrected values, including rounding at exactly .50 and incomes too low to be taxed. An end-to-end `TaxCalculatorTest` checks the corrected output of the whole program.

<sub>[↑ Back to contents](#contents)</sub>

<a id="f2"></a>

### F.2 Two wrong lines in the report

*Made together with [2.4](#s2-4).*

| Report line | Before (wrong) | After (correct) |
| --- | --- | --- |
| Disability contribution (1.5%) | Labelled `Health social security tax`, a translation mistake | Labelled `Disability social security tax` |
| Civil contract, "Already paid tax" | Showed the advance tax | Shows the tax after the allowance, as the label says |

The second fix does not change any number printed today, because the civil contract has no allowance, so both values are the same. It would have shown a wrong number as soon as an allowance was added.

<sub>[↑ Back to contents](#contents)</sub>

<a id="the-result"></a>

## The result

| | Before | After |
| --- | --- | --- |
| Compiles | No (14 errors) | Yes |
| `main` method | 108 lines doing everything | 8 lines that only assemble the program |
| Money | `double`, inexact | `BigDecimal`, exact, rounded as the law requires |
| Tax rules | Partly wrong | Follow Polish rules (a separate fix, see [F.1](#f1)) |
| Tax rates | Bare numbers scattered around | Named, in one place |
| Names | `soc_health1`, `df00`, `taxedIncome0` | `healthContribution`, `roundedTaxableBase` |
| Duplication | About 90% copy-paste between contracts | Shared steps written once |
| Adding a contract type | Edit a big `if/else` | Combine existing parts in one new `TaxStrategies` method |
| Changing input or output | Rewrite `main` | Add one reader or writer class |
| Build | No build tool, compiled by hand | `mvn test` |
| Tests | One hand-run file, loose checks | 75 JUnit tests in 15 files, exact values and edge cases |

<sub>[↑ Back to contents](#contents)</sub>
