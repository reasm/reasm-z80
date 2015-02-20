package org.reasm.z80.assembly.internal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.reasm.AssemblyMessage;

/**
 * Test class for all Z80 family instructions.
 *
 * @author Francis Gagné
 */
@RunWith(Parameterized.class)
public class InstructionsTest extends BaseProgramsTest {

    private static final ArrayList<Object[]> TEST_DATA = new ArrayList<>();

    static {
        // CCF
        addDataItem(" CCF", new byte[] { 0x3F });
        // --> see NOP for more tests

        // CPD
        addDataItem(" CPD", new byte[] { (byte) 0xED, (byte) 0xA9 });
        // --> see NOP for more tests

        // CPDR
        addDataItem(" CPDR", new byte[] { (byte) 0xED, (byte) 0xB9 });
        // --> see NOP for more tests

        // CPI
        addDataItem(" CPI", new byte[] { (byte) 0xED, (byte) 0xA1 });
        // --> see NOP for more tests

        // CPIR
        addDataItem(" CPIR", new byte[] { (byte) 0xED, (byte) 0xB1 });
        // --> see NOP for more tests

        // CPL
        addDataItem(" CPL", new byte[] { 0x2F });
        // --> see NOP for more tests

        // DAA
        addDataItem(" DAA", new byte[] { 0x27 });
        // --> see NOP for more tests

        // DI
        addDataItem(" DI", new byte[] { (byte) 0xF3 });
        // --> see NOP for more tests

        // EI
        addDataItem(" EI", new byte[] { (byte) 0xFB });
        // --> see NOP for more tests

        // EXX
        addDataItem(" EXX", new byte[] { (byte) 0xD9 });
        // --> see NOP for more tests

        // HALT
        addDataItem(" HALT", new byte[] { 0x76 });
        // --> see NOP for more tests

        // IND
        addDataItem(" IND", new byte[] { (byte) 0xED, (byte) 0xAA });
        // --> see NOP for more tests

        // INDR
        addDataItem(" INDR", new byte[] { (byte) 0xED, (byte) 0xBA });
        // --> see NOP for more tests

        // INI
        addDataItem(" INI", new byte[] { (byte) 0xED, (byte) 0xA2 });
        // --> see NOP for more tests

        // INIR
        addDataItem(" INIR", new byte[] { (byte) 0xED, (byte) 0xB2 });
        // --> see NOP for more tests

        // LDD
        addDataItem(" LDD", new byte[] { (byte) 0xED, (byte) 0xA8 });
        // --> see NOP for more tests

        // LDDR
        addDataItem(" LDDR", new byte[] { (byte) 0xED, (byte) 0xB8 });
        // --> see NOP for more tests

        // LDI
        addDataItem(" LDI", new byte[] { (byte) 0xED, (byte) 0xA0 });
        // --> see NOP for more tests

        // LDIR
        addDataItem(" LDIR", new byte[] { (byte) 0xED, (byte) 0xB0 });
        // --> see NOP for more tests

        // NEG
        addDataItem(" NEG", new byte[] { (byte) 0xED, 0x44 });
        // --> see NOP for more tests

        // NOP
        addDataItem(" NOP", new byte[] { 0x00 });
        addDataItem(" NOP A", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" NOP A,A", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" NOP A,A,A", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);

        // OTDR
        addDataItem(" OTDR", new byte[] { (byte) 0xED, (byte) 0xBB });
        // --> see NOP for more tests

        // OTIR
        addDataItem(" OTIR", new byte[] { (byte) 0xED, (byte) 0xB3 });
        // --> see NOP for more tests

        // OUTD
        addDataItem(" OUTD", new byte[] { (byte) 0xED, (byte) 0xAB });
        // --> see NOP for more tests

        // OUTI
        addDataItem(" OUTI", new byte[] { (byte) 0xED, (byte) 0xA3 });
        // --> see NOP for more tests

        // RETI
        addDataItem(" RETI", new byte[] { (byte) 0xED, 0x4D });
        // --> see NOP for more tests

        // RETN
        addDataItem(" RETN", new byte[] { (byte) 0xED, 0x45 });
        // --> see NOP for more tests

        // RLA
        addDataItem(" RLA", new byte[] { 0x17 });
        // --> see NOP for more tests

        // RLCA
        addDataItem(" RLCA", new byte[] { 0x07 });
        // --> see NOP for more tests

        // RLD
        addDataItem(" RLD", new byte[] { (byte) 0xED, 0x6F });
        // --> see NOP for more tests

        // RRA
        addDataItem(" RRA", new byte[] { 0x1F });
        // --> see NOP for more tests

        // RRCA
        addDataItem(" RRCA", new byte[] { 0x0F });
        // --> see NOP for more tests

        // RRD
        addDataItem(" RRD", new byte[] { (byte) 0xED, 0x67 });
        // --> see NOP for more tests

        // SCF
        addDataItem(" SCF", new byte[] { 0x37 });
        // --> see NOP for more tests
    }

    /**
     * Gets the test data for this parameterized test.
     *
     * @return the test data
     */
    @Nonnull
    @Parameters
    public static List<Object[]> data() {
        return TEST_DATA;
    }

    private static void addDataItem(@Nonnull String code, @Nonnull byte[] output) {
        addDataItem(code, output, null);
    }

    private static void addDataItem(@Nonnull String code, @Nonnull byte[] output, @CheckForNull AssemblyMessage expectedMessage) {
        TEST_DATA.add(new Object[] { code, output, expectedMessage });
    }

    /**
     * Initializes a new InstructionsTest.
     *
     * @param code
     *            a line of code containing an instruction
     * @param output
     *            the generated opcode for the instruction
     * @param expectedMessage
     *            an {@link AssemblyMessage} that is expected to be generated while assembling the line of code
     */
    public InstructionsTest(@Nonnull String code, @Nonnull byte[] output, @CheckForNull AssemblyMessage expectedMessage) {
        super(code, 2, output, expectedMessage, null, null);
    }

}
