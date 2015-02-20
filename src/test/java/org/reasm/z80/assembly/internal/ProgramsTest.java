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
 * Test class for short M68000 programs.
 *
 * @author Francis Gagné
 */
@RunWith(Parameterized.class)
public class ProgramsTest extends BaseProgramsTest {

    @Nonnull
    private static final ArrayList<Object[]> TEST_DATA = new ArrayList<>();

    static {
        // undefined mnemonic
        addDataItem(" UNDEFINED", 2, NO_DATA, UNDEFINED_SYMBOL);
        // TODO: Reactivate this test when the DW directive is implemented
        //addDataItem("A: UNDEFINED\n DW A", 3, new byte[] { 0x00, 0x00 }, UNDEFINED_SYMBOL);

        // ! prefix on a block directive
        // TODO: Reactivate this test when IF blocks and the DW directive are implemented
        //addDataItem(" !IF 0\n DW $0123\n !ELSEIF 1\n DW $1234\n !ENDIF", 6, new byte[] { 0x12, 0x34 });

        // continuation characters
        // TODO: Reactivate this test when the DB directive is implemented
        //addDataItem(" DB &\n1", 2, new byte[] { 1 });
        //addDataItem(" D&\nB 1", 2, new byte[] { 1 });
        //addDataItem(" D&\rB 1", 2, new byte[] { 1 });
        //addDataItem(" D&\r\nB 1", 2, new byte[] { 1 });
        //addDataItem(" D&\n B 1", 2, new byte[] { 1 });
        //addDataItem(" D&\n\tB 1", 2, new byte[] { 1 });
        //addDataItem(" D&\n\t\t  B 1", 2, new byte[] { 1 });
        //addDataItem(" DB 1&\n1", 2, new byte[] { 11 });
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

    //private static void addDataItem(@Nonnull String code, int steps, @Nonnull byte[] output) {
    //    addDataItem(code, steps, output, null);
    //}

    private static void addDataItem(@Nonnull String code, int steps, @Nonnull byte[] output,
            @CheckForNull AssemblyMessage expectedMessage) {
        TEST_DATA.add(new Object[] { code, steps, output, expectedMessage });
    }

    /**
     * Initializes a new ProgramsTest.
     *
     * @param code
     *            assembly code to assemble
     * @param steps
     *            the number of steps the program is expected to take to assemble completely
     * @param output
     *            the program's output
     * @param expectedMessage
     *            an {@link AssemblyMessage} that is expected to be generated while assembling the code
     */
    public ProgramsTest(@Nonnull String code, int steps, @Nonnull byte[] output, @CheckForNull AssemblyMessage expectedMessage) {
        super(code, steps, output, expectedMessage, null, null);
    }

}
