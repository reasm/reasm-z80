package org.reasm.z80.assembly.internal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.reasm.AssemblyMessage;
import org.reasm.commons.messages.RelativeBranchTargetOutOfRangeErrorMessage;
import org.reasm.commons.messages.ValueOutOfRangeErrorMessage;
import org.reasm.z80.messages.InvalidConditionErrorMessage;
import org.reasm.z80.messages.InvalidImmediateModeErrorMessage;

/**
 * Test class for all Z80 family instructions.
 *
 * @author Francis Gagné
 */
@RunWith(Parameterized.class)
public class InstructionsTest extends BaseProgramsTest {

    @Nonnull
    private static final AssemblyMessage INVALID_CONDITION_Q = new InvalidConditionErrorMessage("Q");
    @Nonnull
    private static final AssemblyMessage VALUE_OUT_OF_RANGE_10000 = new ValueOutOfRangeErrorMessage(0x10000);
    @Nonnull
    private static final AssemblyMessage VALUE_OUT_OF_RANGE_MINUS_8001 = new ValueOutOfRangeErrorMessage(-0x8001);

    @Nonnull
    private static final ArrayList<Object[]> TEST_DATA = new ArrayList<>();

    static {
        // ADC
        // - ADC A, r
        addDataItem(" ADC A,B", new byte[] { (byte) 0x88 });
        addDataItem(" ADC A,A", new byte[] { (byte) 0x8F });
        // - ADC A, n
        addDataItem(" ADC A,23h", new byte[] { (byte) 0xCE, 0x23 });
        // - ADC A, (HL)
        addDataItem(" ADC A,(HL)", new byte[] { (byte) 0x8E });
        // - ADC A, (IX+d)
        addDataItem(" ADC A,(IX+12h)", new byte[] { (byte) 0xDD, (byte) 0x8E, 0x12 });
        // - ADC A, (IY+d)
        addDataItem(" ADC A,(IY+12h)", new byte[] { (byte) 0xFD, (byte) 0x8E, 0x12 });
        // - ADC HL, ss
        addDataItem(" ADC HL,BC", new byte[] { (byte) 0xED, 0x4A });
        addDataItem(" ADC HL,DE", new byte[] { (byte) 0xED, 0x5A });
        addDataItem(" ADC HL,HL", new byte[] { (byte) 0xED, 0x6A });
        addDataItem(" ADC HL,SP", new byte[] { (byte) 0xED, 0x7A });
        // - invalid forms
        addDataItem(" ADC", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" ADC A", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" ADC A,B,C", new byte[] { (byte) 0x88 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" ADC A,HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" ADC HL,IX", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" ADC HL,IY", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" ADC IX,IX", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" ADC IY,IY", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" ADC B,C", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);

        // ADD
        // - ADD A, r
        addDataItem(" ADD A,B", new byte[] { (byte) 0x80 });
        addDataItem(" ADD A,A", new byte[] { (byte) 0x87 });
        // - ADD A, n
        addDataItem(" ADD A,23h", new byte[] { (byte) 0xC6, 0x23 });
        // - ADD A, (HL)
        addDataItem(" ADD A,(HL)", new byte[] { (byte) 0x86 });
        // - ADD A, (IX+d)
        addDataItem(" ADD A,(IX+12h)", new byte[] { (byte) 0xDD, (byte) 0x86, 0x12 });
        // - ADD A, (IY+d)
        addDataItem(" ADD A,(IY+12h)", new byte[] { (byte) 0xFD, (byte) 0x86, 0x12 });
        // - ADD HL, ss
        addDataItem(" ADD HL,BC", new byte[] { 0x09 });
        addDataItem(" ADD HL,DE", new byte[] { 0x19 });
        addDataItem(" ADD HL,HL", new byte[] { 0x29 });
        addDataItem(" ADD HL,SP", new byte[] { 0x39 });
        // - ADD IX, pp
        addDataItem(" ADD IX,BC", new byte[] { (byte) 0xDD, 0x09 });
        addDataItem(" ADD IX,DE", new byte[] { (byte) 0xDD, 0x19 });
        addDataItem(" ADD IX,IX", new byte[] { (byte) 0xDD, 0x29 });
        addDataItem(" ADD IX,SP", new byte[] { (byte) 0xDD, 0x39 });
        // - ADD IY, rr
        addDataItem(" ADD IY,BC", new byte[] { (byte) 0xFD, 0x09 });
        addDataItem(" ADD IY,DE", new byte[] { (byte) 0xFD, 0x19 });
        addDataItem(" ADD IY,IY", new byte[] { (byte) 0xFD, 0x29 });
        addDataItem(" ADD IY,SP", new byte[] { (byte) 0xFD, 0x39 });
        // - invalid forms
        addDataItem(" ADD", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" ADD A", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" ADD A,B,C", new byte[] { (byte) 0x80 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" ADD A,HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" ADD HL,(HL)", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" ADD HL,IX", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" ADD HL,IY", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" ADD IX,HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" ADD IY,HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" ADD IX,IY", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" ADD IY,IX", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" ADD B,C", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);

        // AND
        addDataItem(" AND B", new byte[] { (byte) 0xA0 });
        addDataItem(" AND 23h", new byte[] { (byte) 0xE6, 0x23 });
        // --> see SUB for more tests

        // BIT
        // - BIT b, r
        addDataItem(" BIT 0,B", new byte[] { (byte) 0xCB, 0x40 });
        addDataItem(" BIT 0,A", new byte[] { (byte) 0xCB, 0x47 });
        addDataItem(" BIT 1,B", new byte[] { (byte) 0xCB, 0x48 });
        addDataItem(" BIT 2,B", new byte[] { (byte) 0xCB, 0x50 });
        addDataItem(" BIT 4,B", new byte[] { (byte) 0xCB, 0x60 });
        addDataItem(" BIT 7,B", new byte[] { (byte) 0xCB, 0x78 });
        addDataItem(" BIT 8,B", new byte[] { (byte) 0xCB, 0x40 }, new ValueOutOfRangeErrorMessage(8));
        addDataItem(" BIT -1,B", new byte[] { (byte) 0xCB, 0x78 }, new ValueOutOfRangeErrorMessage(-1));
        // - BIT b, (HL)
        addDataItem(" BIT 0,(HL)", new byte[] { (byte) 0xCB, 0x46 });
        addDataItem(" BIT 7,(HL)", new byte[] { (byte) 0xCB, 0x7E });
        // - BIT b, (IX+d)
        addDataItem(" BIT 0,(IX+12h)", new byte[] { (byte) 0xDD, (byte) 0xCB, 0x12, 0x46 });
        addDataItem(" BIT 7,(IX+12h)", new byte[] { (byte) 0xDD, (byte) 0xCB, 0x12, 0x7E });
        // - BIT b, (IY+d)
        addDataItem(" BIT 0,(IY+12h)", new byte[] { (byte) 0xFD, (byte) 0xCB, 0x12, 0x46 });
        addDataItem(" BIT 7,(IY+12h)", new byte[] { (byte) 0xFD, (byte) 0xCB, 0x12, 0x7E });
        // - invalid forms
        addDataItem(" BIT", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" BIT 0", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" BIT 0,B,B", new byte[] { (byte) 0xCB, 0x40 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" BIT 0,HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" BIT B,B", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);

        // CALL
        // - CALL nn
        addDataItem(" CALL 0", new byte[] { (byte) 0xCD, 0x00, 0x00 });
        addDataItem(" CALL 1234h", new byte[] { (byte) 0xCD, 0x34, 0x12 });
        addDataItem(" CALL 0FFFFh", new byte[] { (byte) 0xCD, (byte) 0xFF, (byte) 0xFF });
        addDataItem(" CALL 10000h", new byte[] { (byte) 0xCD, 0x00, 0x00 }, VALUE_OUT_OF_RANGE_10000);
        addDataItem(" CALL -8000h", new byte[] { (byte) 0xCD, 0x00, (byte) 0x80 });
        addDataItem(" CALL -8001h", new byte[] { (byte) 0xCD, (byte) 0xFF, 0x7F }, VALUE_OUT_OF_RANGE_MINUS_8001);
        // - CALL cc, nn
        addDataItem(" CALL NZ,1234h", new byte[] { (byte) 0xC4, 0x34, 0x12 });
        addDataItem(" CALL Z,1234h", new byte[] { (byte) 0xCC, 0x34, 0x12 });
        addDataItem(" CALL NC,1234h", new byte[] { (byte) 0xD4, 0x34, 0x12 });
        addDataItem(" CALL C,1234h", new byte[] { (byte) 0xDC, 0x34, 0x12 });
        addDataItem(" CALL PO,1234h", new byte[] { (byte) 0xE4, 0x34, 0x12 });
        addDataItem(" CALL PE,1234h", new byte[] { (byte) 0xEC, 0x34, 0x12 });
        addDataItem(" CALL P,1234h", new byte[] { (byte) 0xF4, 0x34, 0x12 });
        addDataItem(" CALL M,1234h", new byte[] { (byte) 0xFC, 0x34, 0x12 });
        addDataItem(" CALL Q,1234h", new byte[] { (byte) 0xC4, 0x34, 0x12 }, INVALID_CONDITION_Q);
        // - invalid forms
        addDataItem(" CALL", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" CALL NZ,1234h,0", new byte[] { (byte) 0xC4, 0x34, 0x12 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" CALL A", new byte[] { (byte) 0xCD, 0x00, 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" CALL (HL)", new byte[] { (byte) 0xCD, 0x00, 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" CALL (IX)", new byte[] { (byte) 0xCD, 0x00, 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" CALL (IY)", new byte[] { (byte) 0xCD, 0x00, 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" CALL (IX+0)", new byte[] { (byte) 0xCD, 0x00, 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" CALL (IX+12h)", new byte[] { (byte) 0xCD, 0x00, 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" CALL (IY+0)", new byte[] { (byte) 0xCD, 0x00, 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" CALL (IY+12h)", new byte[] { (byte) 0xCD, 0x00, 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" CALL NZ,A", new byte[] { (byte) 0xC4, 0x00, 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);

        // CCF
        addDataItem(" CCF", new byte[] { 0x3F });
        // --> see NOP for more tests

        // CP
        addDataItem(" CP B", new byte[] { (byte) 0xB8 });
        addDataItem(" CP 23h", new byte[] { (byte) 0xFE, 0x23 });
        // --> see SUB for more tests

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

        // DEC
        addDataItem(" DEC B", new byte[] { 0x05 });
        addDataItem(" DEC BC", new byte[] { 0x0B });
        // --> see INC for more tests

        // DI
        addDataItem(" DI", new byte[] { (byte) 0xF3 });
        // --> see NOP for more tests

        // DJNZ
        addDataItem(" DJNZ 0", new byte[] { 0x10, (byte) 0xFE });
        addDataItem(" DJNZ 23h", new byte[] { 0x10, 0x21 });
        addDataItem(" DJNZ 81h", new byte[] { 0x10, 0x7F });
        addDataItem(" DJNZ 82h", new byte[] { 0x10, (byte) 0x80 }, new RelativeBranchTargetOutOfRangeErrorMessage(0x80));
        addDataItem(" DJNZ -7Eh", new byte[] { 0x10, (byte) 0x80 });
        addDataItem(" DJNZ -7Fh", new byte[] { 0x10, 0x7F }, new RelativeBranchTargetOutOfRangeErrorMessage(-0x81));
        // - invalid forms
        addDataItem(" DJNZ", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" DJNZ 23h,0", new byte[] { 0x10, 0x21 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" DJNZ A", new byte[] { 0x10, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" DJNZ (HL)", new byte[] { 0x10, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" DJNZ (IX)", new byte[] { 0x10, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" DJNZ (IY)", new byte[] { 0x10, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" DJNZ (IX+0)", new byte[] { 0x10, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" DJNZ (IX+12h)", new byte[] { 0x10, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" DJNZ (IY+0)", new byte[] { 0x10, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" DJNZ (IY+12h)", new byte[] { 0x10, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);

        // EI
        addDataItem(" EI", new byte[] { (byte) 0xFB });
        // --> see NOP for more tests

        // EX
        addDataItem(" EX", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" EX DE", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" EX DE, HL", new byte[] { (byte) 0xEB });
        addDataItem(" EX AF, AF'", new byte[] { 0x08 });
        addDataItem(" EX (SP), HL", new byte[] { (byte) 0xE3 });
        addDataItem(" EX (SP), IX", new byte[] { (byte) 0xDD, (byte) 0xE3 });
        addDataItem(" EX (SP), IY", new byte[] { (byte) 0xFD, (byte) 0xE3 });
        addDataItem(" EX DE, SP", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" EX AF, HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" EX (SP), DE", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" EX HL, DE", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" EX AF', AF", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" EX HL, (SP)", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" EX IX, (SP)", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" EX IY, (SP)", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" EX DE, HL, HL", new byte[] { (byte) 0xEB }, WRONG_NUMBER_OF_OPERANDS);

        // EXX
        addDataItem(" EXX", new byte[] { (byte) 0xD9 });
        // --> see NOP for more tests

        // HALT
        addDataItem(" HALT", new byte[] { 0x76 });
        // --> see NOP for more tests

        // IM
        addDataItem(" IM", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" IM 0", new byte[] { (byte) 0xED, 0x46 });
        addDataItem(" IM 1", new byte[] { (byte) 0xED, 0x56 });
        addDataItem(" IM 2", new byte[] { (byte) 0xED, 0x5E });
        addDataItem(" IM 3", new byte[] { (byte) 0xED, 0x46 }, new InvalidImmediateModeErrorMessage());
        addDataItem(" IM 0,0", new byte[] { (byte) 0xED, 0x46 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" IM A", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);

        // INC
        // - INC r
        addDataItem(" INC B", new byte[] { 0x04 });
        addDataItem(" INC A", new byte[] { 0x3C });
        // - INC (HL)
        addDataItem(" INC (HL)", new byte[] { 0x34 });
        // - INC (IX+d)
        addDataItem(" INC (IX+12h)", new byte[] { (byte) 0xDD, 0x34, 0x12 });
        // - INC (IY+d)
        addDataItem(" INC (IY+12h)", new byte[] { (byte) 0xFD, 0x34, 0x12 });
        // - INC ss
        addDataItem(" INC BC", new byte[] { 0x03 });
        addDataItem(" INC DE", new byte[] { 0x13 });
        addDataItem(" INC HL", new byte[] { 0x23 });
        addDataItem(" INC SP", new byte[] { 0x33 });
        // - INC IX
        addDataItem(" INC IX", new byte[] { (byte) 0xDD, 0x23 });
        // - INC IY
        addDataItem(" INC IY", new byte[] { (byte) 0xFD, 0x23 });
        // - invalid forms
        addDataItem(" INC", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" INC B,C", new byte[] { 0x04 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" INC 23h", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" INC (1234h)", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);

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

        // JP
        // - JP nn
        addDataItem(" JP 0", new byte[] { (byte) 0xC3, 0x00, 0x00 });
        addDataItem(" JP 1234h", new byte[] { (byte) 0xC3, 0x34, 0x12 });
        addDataItem(" JP 0FFFFh", new byte[] { (byte) 0xC3, (byte) 0xFF, (byte) 0xFF });
        addDataItem(" JP 10000h", new byte[] { (byte) 0xC3, 0x00, 0x00 }, VALUE_OUT_OF_RANGE_10000);
        addDataItem(" JP -8000h", new byte[] { (byte) 0xC3, 0x00, (byte) 0x80 });
        addDataItem(" JP -8001h", new byte[] { (byte) 0xC3, (byte) 0xFF, 0x7F }, VALUE_OUT_OF_RANGE_MINUS_8001);
        // - JP cc, nn
        addDataItem(" JP NZ,1234h", new byte[] { (byte) 0xC2, 0x34, 0x12 });
        addDataItem(" JP Z,1234h", new byte[] { (byte) 0xCA, 0x34, 0x12 });
        addDataItem(" JP NC,1234h", new byte[] { (byte) 0xD2, 0x34, 0x12 });
        addDataItem(" JP C,1234h", new byte[] { (byte) 0xDA, 0x34, 0x12 });
        addDataItem(" JP PO,1234h", new byte[] { (byte) 0xE2, 0x34, 0x12 });
        addDataItem(" JP PE,1234h", new byte[] { (byte) 0xEA, 0x34, 0x12 });
        addDataItem(" JP P,1234h", new byte[] { (byte) 0xF2, 0x34, 0x12 });
        addDataItem(" JP M,1234h", new byte[] { (byte) 0xFA, 0x34, 0x12 });
        addDataItem(" JP Q,1234h", new byte[] { (byte) 0xC2, 0x34, 0x12 }, INVALID_CONDITION_Q);
        // - JP (HL)
        addDataItem(" JP (HL)", new byte[] { (byte) 0xE9 });
        // - JP (IX)
        addDataItem(" JP (IX)", new byte[] { (byte) 0xDD, (byte) 0xE9 });
        // - JP (IY)
        addDataItem(" JP (IY)", new byte[] { (byte) 0xFD, (byte) 0xE9 });
        // - invalid forms
        addDataItem(" JP", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" JP NZ,1234h,0", new byte[] { (byte) 0xC2, 0x34, 0x12 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" JP A", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" JP (IX+0)", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" JP (IX+12h)", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" JP (IY+0)", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" JP (IY+12h)", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" JP NZ,A", new byte[] { (byte) 0xC2, 0x00, 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);

        // JR
        // - JR e
        addDataItem(" JR 0", new byte[] { 0x18, (byte) 0xFE });
        addDataItem(" JR 23h", new byte[] { 0x18, 0x21 });
        addDataItem(" JR 81h", new byte[] { 0x18, 0x7F });
        addDataItem(" JR 82h", new byte[] { 0x18, (byte) 0x80 }, new RelativeBranchTargetOutOfRangeErrorMessage(0x80));
        addDataItem(" JR -7Eh", new byte[] { 0x18, (byte) 0x80 });
        addDataItem(" JR -7Fh", new byte[] { 0x18, 0x7F }, new RelativeBranchTargetOutOfRangeErrorMessage(-0x81));
        // - JR C, e
        // - JR NC, e
        // - JR Z, e
        // - JR NZ, e
        addDataItem(" JR NZ,23h", new byte[] { 0x20, 0x21 });
        addDataItem(" JR Z,23h", new byte[] { 0x28, 0x21 });
        addDataItem(" JR NC,23h", new byte[] { 0x30, 0x21 });
        addDataItem(" JR C,23h", new byte[] { 0x38, 0x21 });
        addDataItem(" JR PO,23h", new byte[] { 0x20, 0x21 }, new InvalidConditionErrorMessage("PO"));
        addDataItem(" JR PE,23h", new byte[] { 0x28, 0x21 }, new InvalidConditionErrorMessage("PE"));
        addDataItem(" JR P,23h", new byte[] { 0x30, 0x21 }, new InvalidConditionErrorMessage("P"));
        addDataItem(" JR M,23h", new byte[] { 0x38, 0x21 }, new InvalidConditionErrorMessage("M"));
        addDataItem(" JR Q,23h", new byte[] { 0x20, 0x21 }, INVALID_CONDITION_Q);
        // - invalid forms
        addDataItem(" JR", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" JR NZ,23h,0", new byte[] { 0x20, 0x21 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" JR A", new byte[] { 0x18, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" JR (HL)", new byte[] { 0x18, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" JR (IX)", new byte[] { 0x18, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" JR (IY)", new byte[] { 0x18, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" JR (IX+0)", new byte[] { 0x18, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" JR (IX+12h)", new byte[] { 0x18, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" JR (IY+0)", new byte[] { 0x18, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" JR (IY+12h)", new byte[] { 0x18, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" JR NZ,A", new byte[] { 0x20, (byte) 0xFE }, ADDRESSING_MODE_NOT_ALLOWED_HERE);

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

        // OR
        addDataItem(" OR B", new byte[] { (byte) 0xB0 });
        addDataItem(" OR 23h", new byte[] { (byte) 0xF6, 0x23 });
        // --> see SUB for more tests

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

        // POP
        addDataItem(" POP BC", new byte[] { (byte) 0xC1 });
        // --> see PUSH for more tests

        // PUSH
        addDataItem(" PUSH", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" PUSH BC", new byte[] { (byte) 0xC5 });
        addDataItem(" PUSH DE", new byte[] { (byte) 0xD5 });
        addDataItem(" PUSH HL", new byte[] { (byte) 0xE5 });
        addDataItem(" PUSH AF", new byte[] { (byte) 0xF5 });
        addDataItem(" PUSH IX", new byte[] { (byte) 0xDD, (byte) 0xE5 });
        addDataItem(" PUSH IY", new byte[] { (byte) 0xFD, (byte) 0xE5 });
        addDataItem(" PUSH SP", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);
        addDataItem(" PUSH BC,DE", new byte[] { (byte) 0xC5 }, WRONG_NUMBER_OF_OPERANDS);

        // RES
        addDataItem(" RES 0,B", new byte[] { (byte) 0xCB, (byte) 0x80 });
        // --> see BIT for more tests

        // RET
        // - RET
        addDataItem(" RET", new byte[] { (byte) 0xC9 });
        // - RET cc
        addDataItem(" RET NZ", new byte[] { (byte) 0xC0 });
        addDataItem(" RET Z", new byte[] { (byte) 0xC8 });
        addDataItem(" RET NC", new byte[] { (byte) 0xD0 });
        addDataItem(" RET C", new byte[] { (byte) 0xD8 });
        addDataItem(" RET PO", new byte[] { (byte) 0xE0 });
        addDataItem(" RET PE", new byte[] { (byte) 0xE8 });
        addDataItem(" RET P", new byte[] { (byte) 0xF0 });
        addDataItem(" RET M", new byte[] { (byte) 0xF8 });
        addDataItem(" RET Q", new byte[] { (byte) 0xC0 }, INVALID_CONDITION_Q);
        // - invalid forms
        addDataItem(" RET NZ,0", new byte[] { (byte) 0xC0 }, WRONG_NUMBER_OF_OPERANDS);

        // RETI
        addDataItem(" RETI", new byte[] { (byte) 0xED, 0x4D });
        // --> see NOP for more tests

        // RETN
        addDataItem(" RETN", new byte[] { (byte) 0xED, 0x45 });
        // --> see NOP for more tests

        // RL
        addDataItem(" RL B", new byte[] { (byte) 0xCB, 0x10 });
        // --> see RLC for more tests

        // RLA
        addDataItem(" RLA", new byte[] { 0x17 });
        // --> see NOP for more tests

        // RLC
        // - RLC r
        addDataItem(" RLC B", new byte[] { (byte) 0xCB, 0x00 });
        addDataItem(" RLC A", new byte[] { (byte) 0xCB, 0x07 });
        // - RLC (HL)
        addDataItem(" RLC (HL)", new byte[] { (byte) 0xCB, 0x06 });
        // - RLC (IX+d)
        addDataItem(" RLC (IX+12h)", new byte[] { (byte) 0xDD, (byte) 0xCB, 0x12, 0x06 });
        // - RLC (IY+d)
        addDataItem(" RLC (IY+12h)", new byte[] { (byte) 0xFD, (byte) 0xCB, 0x12, 0x06 });
        // - invalid forms
        addDataItem(" RLC", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" RLC B,B", new byte[] { (byte) 0xCB, 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" RLC HL", new byte[] { 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);

        // RLCA
        addDataItem(" RLCA", new byte[] { 0x07 });
        // --> see NOP for more tests

        // RLD
        addDataItem(" RLD", new byte[] { (byte) 0xED, 0x6F });
        // --> see NOP for more tests

        // RR
        addDataItem(" RR B", new byte[] { (byte) 0xCB, 0x18 });
        // --> see RLC for more tests

        // RRA
        addDataItem(" RRA", new byte[] { 0x1F });
        // --> see NOP for more tests

        // RRC
        addDataItem(" RRC B", new byte[] { (byte) 0xCB, 0x08 });
        // --> see RLC for more tests

        // RRCA
        addDataItem(" RRCA", new byte[] { 0x0F });
        // --> see NOP for more tests

        // RRD
        addDataItem(" RRD", new byte[] { (byte) 0xED, 0x67 });
        // --> see NOP for more tests

        // SBC
        addDataItem(" SBC A,B", new byte[] { (byte) 0x98 });
        addDataItem(" SBC A,23h", new byte[] { (byte) 0xDE, 0x23 });
        addDataItem(" SBC HL,BC", new byte[] { (byte) 0xED, 0x42 });
        // --> see ADC for more tests

        // SCF
        addDataItem(" SCF", new byte[] { 0x37 });
        // --> see NOP for more tests

        // SET
        addDataItem(" SET 0,B", new byte[] { (byte) 0xCB, (byte) 0xC0 });
        // --> see BIT for more tests

        // SLA
        addDataItem(" SLA B", new byte[] { (byte) 0xCB, 0x20 });
        // --> see RLC for more tests

        // SRA
        addDataItem(" SRA B", new byte[] { (byte) 0xCB, 0x28 });
        // --> see RLC for more tests

        // SRL
        addDataItem(" SRL B", new byte[] { (byte) 0xCB, 0x38 });
        // --> see RLC for more tests

        // SUB
        // - SUB r
        addDataItem(" SUB B", new byte[] { (byte) 0x90 });
        addDataItem(" SUB A", new byte[] { (byte) 0x97 });
        // - SUB n
        addDataItem(" SUB 23h", new byte[] { (byte) 0xD6, 0x23 });
        // - SUB (HL)
        addDataItem(" SUB (HL)", new byte[] { (byte) 0x96 });
        // - SUB (IX+d)
        addDataItem(" SUB (IX+12h)", new byte[] { (byte) 0xDD, (byte) 0x96, 0x12 });
        // - SUB (IY+d)
        addDataItem(" SUB (IY+12h)", new byte[] { (byte) 0xFD, (byte) 0x96, 0x12 });
        // - invalid forms
        addDataItem(" SUB", new byte[] { 0x00 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" SUB B,C", new byte[] { (byte) 0x90 }, WRONG_NUMBER_OF_OPERANDS);
        addDataItem(" SUB HL", new byte[] { (byte) 0x00 }, ADDRESSING_MODE_NOT_ALLOWED_HERE);

        // XOR
        addDataItem(" XOR B", new byte[] { (byte) 0xA8 });
        addDataItem(" XOR 23h", new byte[] { (byte) 0xEE, 0x23 });
        // --> see SUB for more tests
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
