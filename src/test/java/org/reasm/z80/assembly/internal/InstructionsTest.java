package org.reasm.z80.assembly.internal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.reasm.AssemblyMessage;
import org.reasm.commons.messages.ValueOutOfRangeErrorMessage;

/**
 * Test class for all Z80 family instructions.
 *
 * @author Francis Gagné
 */
@RunWith(Parameterized.class)
public class InstructionsTest extends BaseProgramsTest {

    @Nonnull
    private static final AssemblyMessage VALUE_OUT_OF_RANGE_10000 = new ValueOutOfRangeErrorMessage(0x10000);

    @Nonnull
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

        // LD
        // - LD r, r'
        addDataItem(" LD B,B", new byte[] { 0x40 });
        addDataItem(" LD B,C", new byte[] { 0x41 });
        addDataItem(" LD B,D", new byte[] { 0x42 });
        addDataItem(" LD B,E", new byte[] { 0x43 });
        addDataItem(" LD B,H", new byte[] { 0x44 });
        addDataItem(" LD B,L", new byte[] { 0x45 });
        addDataItem(" LD B,A", new byte[] { 0x47 });
        addDataItem(" LD C,B", new byte[] { 0x48 });
        addDataItem(" LD D,B", new byte[] { 0x50 });
        addDataItem(" LD E,B", new byte[] { 0x58 });
        addDataItem(" LD H,B", new byte[] { 0x60 });
        addDataItem(" LD L,B", new byte[] { 0x68 });
        addDataItem(" LD A,B", new byte[] { 0x78 });
        // - LD r, n
        addDataItem(" LD B,0", new byte[] { 0x06, 0x00 });
        addDataItem(" LD A,0", new byte[] { 0x3E, 0x00 });
        addDataItem(" LD B,0FFh", new byte[] { 0x06, (byte) 0xFF });
        addDataItem(" LD B,100h", new byte[] { 0x06, 0x00 }, new ValueOutOfRangeErrorMessage(0x100));
        addDataItem(" LD B,-80h", new byte[] { 0x06, (byte) 0x80 });
        addDataItem(" LD B,-81h", new byte[] { 0x06, 0x7F }, new ValueOutOfRangeErrorMessage(-0x81));
        // - LD r, (HL)
        addDataItem(" LD B,(HL)", new byte[] { 0x46 });
        addDataItem(" LD A,(HL)", new byte[] { 0x7E });
        // - LD r, (IX+d)
        addDataItem(" LD B,(IX)", new byte[] { (byte) 0xDD, 0x46, 0x00 });
        addDataItem(" LD B,(IX+12h)", new byte[] { (byte) 0xDD, 0x46, 0x12 });
        addDataItem(" LD A,(IX+12h)", new byte[] { (byte) 0xDD, 0x7E, 0x12 });
        // - LD r, (IY+d)
        addDataItem(" LD B,(IY)", new byte[] { (byte) 0xFD, 0x46, 0x00 });
        addDataItem(" LD B,(IY+12h)", new byte[] { (byte) 0xFD, 0x46, 0x12 });
        addDataItem(" LD A,(IY+12h)", new byte[] { (byte) 0xFD, 0x7E, 0x12 });
        // - LD (HL), r
        addDataItem(" LD (HL),B", new byte[] { 0x70 });
        addDataItem(" LD (HL),A", new byte[] { 0x77 });
        // - LD (IX+d), r
        addDataItem(" LD (IX),B", new byte[] { (byte) 0xDD, 0x70, 0x00 });
        addDataItem(" LD (IX+12h),B", new byte[] { (byte) 0xDD, 0x70, 0x12 });
        addDataItem(" LD (IX+12h),A", new byte[] { (byte) 0xDD, 0x77, 0x12 });
        // - LD (IY+d), r
        addDataItem(" LD (IY),B", new byte[] { (byte) 0xFD, 0x70, 0x00 });
        addDataItem(" LD (IY+12h),B", new byte[] { (byte) 0xFD, 0x70, 0x12 });
        addDataItem(" LD (IY+12h),A", new byte[] { (byte) 0xFD, 0x77, 0x12 });
        // - LD (HL), n
        addDataItem(" LD (HL),23h", new byte[] { 0x36, 0x23 });
        // - LD (IX+d), n
        addDataItem(" LD (IX+12h),23h", new byte[] { (byte) 0xDD, 0x36, 0x12, 0x23 });
        // - LD (IY+d), n
        addDataItem(" LD (IY+12h),23h", new byte[] { (byte) 0xFD, 0x36, 0x12, 0x23 });
        // - LD A, (BC)
        addDataItem(" LD A,(BC)", new byte[] { 0x0A });
        // - LD A, (DE)
        addDataItem(" LD A,(DE)", new byte[] { 0x1A });
        // - LD A, (nn)
        addDataItem(" LD A,(0)", new byte[] { 0x3A, 0x00, 0x00 });
        addDataItem(" LD A,(1234h)", new byte[] { 0x3A, 0x34, 0x12 });
        addDataItem(" LD A,(0FFFFh)", new byte[] { 0x3A, (byte) 0xFF, (byte) 0xFF });
        addDataItem(" LD A,(10000h)", new byte[] { 0x3A, 0x00, 0x00 }, VALUE_OUT_OF_RANGE_10000);
        // - LD (BC), A
        addDataItem(" LD (BC),A", new byte[] { 0x02 });
        // - LD (DE), A
        addDataItem(" LD (DE),A", new byte[] { 0x12 });
        // - LD (nn), A
        addDataItem(" LD (0),A", new byte[] { 0x32, 0x00, 0x00 });
        addDataItem(" LD (1234h),A", new byte[] { 0x32, 0x34, 0x12 });
        addDataItem(" LD (0FFFFh),A", new byte[] { 0x32, (byte) 0xFF, (byte) 0xFF });
        addDataItem(" LD (10000h),A", new byte[] { 0x32, 0x00, 0x00 }, VALUE_OUT_OF_RANGE_10000);
        // - LD A, I
        addDataItem(" LD A,I", new byte[] { (byte) 0xED, 0x57 });
        // - LD A, R
        addDataItem(" LD A,R", new byte[] { (byte) 0xED, 0x5F });
        // - LD I, A
        addDataItem(" LD I,A", new byte[] { (byte) 0xED, 0x47 });
        // - LD R, A
        addDataItem(" LD R,A", new byte[] { (byte) 0xED, 0x4F });
        // - LD dd, nn
        addDataItem(" LD BC,0", new byte[] { 0x01, 0x00, 0x00 });
        addDataItem(" LD BC,1234h", new byte[] { 0x01, 0x34, 0x12 });
        addDataItem(" LD BC,0FFFFh", new byte[] { 0x01, (byte) 0xFF, (byte) 0xFF });
        addDataItem(" LD BC,10000h", new byte[] { 0x01, 0x00, 0x00 }, VALUE_OUT_OF_RANGE_10000);
        addDataItem(" LD DE,1234h", new byte[] { 0x11, 0x34, 0x12 });
        addDataItem(" LD HL,1234h", new byte[] { 0x21, 0x34, 0x12 });
        addDataItem(" LD SP,1234h", new byte[] { 0x31, 0x34, 0x12 });
        // - LD IX, nn
        addDataItem(" LD IX,1234h", new byte[] { (byte) 0xDD, 0x21, 0x34, 0x12 });
        // - LD IY, nn
        addDataItem(" LD IY,1234h", new byte[] { (byte) 0xFD, 0x21, 0x34, 0x12 });
        // - LD HL, (nn)
        addDataItem(" LD HL,(0)", new byte[] { 0x2A, 0x00, 0x00 });
        addDataItem(" LD HL,(1234h)", new byte[] { 0x2A, 0x34, 0x12 });
        addDataItem(" LD HL,(0FFFFh)", new byte[] { 0x2A, (byte) 0xFF, (byte) 0xFF });
        addDataItem(" LD HL,(10000h)", new byte[] { 0x2A, 0x00, 0x00 }, VALUE_OUT_OF_RANGE_10000);
        // - LD dd, (nn)
        addDataItem(" LD BC,(0)", new byte[] { (byte) 0xED, 0x4B, 0x00, 0x00 });
        addDataItem(" LD BC,(1234h)", new byte[] { (byte) 0xED, 0x4B, 0x34, 0x12 });
        addDataItem(" LD BC,(0FFFFh)", new byte[] { (byte) 0xED, 0x4B, (byte) 0xFF, (byte) 0xFF });
        addDataItem(" LD BC,(10000h)", new byte[] { (byte) 0xED, 0x4B, 0x00, 0x00 }, VALUE_OUT_OF_RANGE_10000);
        addDataItem(" LD DE,(1234h)", new byte[] { (byte) 0xED, 0x5B, 0x34, 0x12 });
        addDataItem(" LD SP,(1234h)", new byte[] { (byte) 0xED, 0x7B, 0x34, 0x12 });
        // - LD IX, (nn)
        addDataItem(" LD IX,(1234h)", new byte[] { (byte) 0xDD, 0x2A, 0x34, 0x12 });
        // - LD IY, (nn)
        addDataItem(" LD IY,(1234h)", new byte[] { (byte) 0xFD, 0x2A, 0x34, 0x12 });
        // - LD (nn), HL
        addDataItem(" LD (0),HL", new byte[] { 0x22, 0x00, 0x00 });
        addDataItem(" LD (1234h),HL", new byte[] { 0x22, 0x34, 0x12 });
        addDataItem(" LD (0FFFFh),HL", new byte[] { 0x22, (byte) 0xFF, (byte) 0xFF });
        addDataItem(" LD (10000h),HL", new byte[] { 0x22, 0x00, 0x00 }, VALUE_OUT_OF_RANGE_10000);
        // - LD (nn), dd
        addDataItem(" LD (0),BC", new byte[] { (byte) 0xED, 0x43, 0x00, 0x00 });
        addDataItem(" LD (1234h),BC", new byte[] { (byte) 0xED, 0x43, 0x34, 0x12 });
        addDataItem(" LD (0FFFFh),BC", new byte[] { (byte) 0xED, 0x43, (byte) 0xFF, (byte) 0xFF });
        addDataItem(" LD (10000h),BC", new byte[] { (byte) 0xED, 0x43, 0x00, 0x00 }, VALUE_OUT_OF_RANGE_10000);
        addDataItem(" LD (1234h),DE", new byte[] { (byte) 0xED, 0x53, 0x34, 0x12 });
        addDataItem(" LD (1234h),SP", new byte[] { (byte) 0xED, 0x73, 0x34, 0x12 });
        // - LD (nn), IX
        addDataItem(" LD (1234h),IX", new byte[] { (byte) 0xDD, 0x22, 0x34, 0x12 });
        // - LD (nn), IY
        addDataItem(" LD (1234h),IY", new byte[] { (byte) 0xFD, 0x22, 0x34, 0x12 });
        // - LD SP, HL
        addDataItem(" LD SP,HL", new byte[] { (byte) 0xF9 });
        // - LD SP, IX
        addDataItem(" LD SP,IX", new byte[] { (byte) 0xDD, (byte) 0xF9 });
        // - LD SP, IY
        addDataItem(" LD SP,IY", new byte[] { (byte) 0xFD, (byte) 0xF9 });
        // - invalid forms
        addDataItem(" LD", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" LD B", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" LD (HL),(HL)", new byte[] { 0x76 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD (HL),(IX+12h)", new byte[] { (byte) 0xDD, 0x76, 0x12 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD (HL),(IY+12h)", new byte[] { (byte) 0xFD, 0x76, 0x12 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD (IX+12h),(HL)", new byte[] { (byte) 0xDD, 0x76, 0x12 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD (IY+12h),(HL)", new byte[] { (byte) 0xFD, 0x76, 0x12 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD (IX+12h),HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD (BC),HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD (DE),HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD A,HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD I,HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD R,HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD HL,HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD IX,HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD (1234h),B", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD SP,B", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD AF,HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD AF',HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" LD B,B,B", new byte[] { 0x40 }, WRONG_NUMBER_OF_OPERANDS);

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
