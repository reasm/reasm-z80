package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * The <code>ADD</code>, <code>ADC</code>, <code>SUB</code>, <code>SBC</code>, <code>AND</code>, <code>XOR</code>, <code>OR</code>
 * and <code>CP</code> instructions.
 *
 * @author Francis Gagné
 */
@Immutable
class BinaryArithmeticLogicalInstruction extends Mnemonic {

    @Nonnull
    static final BinaryArithmeticLogicalInstruction ADD = new BinaryArithmeticLogicalInstruction(0b10_000_000);
    @Nonnull
    static final BinaryArithmeticLogicalInstruction ADC = new BinaryArithmeticLogicalInstruction(0b10_001_000);
    @Nonnull
    static final BinaryArithmeticLogicalInstruction SUB = new BinaryArithmeticLogicalInstruction(0b10_010_000);
    @Nonnull
    static final BinaryArithmeticLogicalInstruction SBC = new BinaryArithmeticLogicalInstruction(0b10_011_000);
    @Nonnull
    static final BinaryArithmeticLogicalInstruction AND = new BinaryArithmeticLogicalInstruction(0b10_100_000);
    @Nonnull
    static final BinaryArithmeticLogicalInstruction XOR = new BinaryArithmeticLogicalInstruction(0b10_101_000);
    @Nonnull
    static final BinaryArithmeticLogicalInstruction OR = new BinaryArithmeticLogicalInstruction(0b10_110_000);
    @Nonnull
    static final BinaryArithmeticLogicalInstruction CP = new BinaryArithmeticLogicalInstruction(0b10_111_000);

    private final int opcode;

    private BinaryArithmeticLogicalInstruction(int opcode) {
        this.opcode = opcode;
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        if (!context.requireNumberOfOperands(1)) {
            context.appendByte((byte) 0x00);
            return;
        }

        final EffectiveAddress ea = context.ea0;

        context.getEffectiveAddress(0, ea);

        if (this.assembleCommonOrIndexedOrImmediate(context, ea)) {
            return;
        }

        context.addressingModeNotAllowed();
    }

    boolean assembleCommonOrIndexedOrImmediate(@Nonnull Z80AssemblyContext context, @Nonnull EffectiveAddress ea)
            throws IOException {
        final AddressingMode addressingMode = ea.addressingMode;

        if (addressingMode.isCommon()) {
            // ADD/ADC/SBC A, r
            // SUB/AND/XOR/OR/CP r
            // ADD/ADC/SBC A, (HL)
            // ADD/ADC/SUB/SBC/AND/XOR/OR/CP (HL)
            context.appendByte((byte) (this.opcode | addressingMode.value));
            return true;
        }

        if (addressingMode.isIndexed()) {
            // ADD/ADC/SBC A, (IX+d)
            // ADD/ADC/SUB/SBC/AND/XOR/OR/CP (IX+d)
            // ADD/ADC/SBC A, (IY+d)
            // ADD/ADC/SUB/SBC/AND/XOR/OR/CP (IY+d)
            context.appendByte((byte) addressingMode.value);
            context.appendByte((byte) (this.opcode | 0b00_000_110));
            context.appendByte(ea.displacement);
            return true;
        }

        if (addressingMode == AddressingMode.IMMEDIATE) {
            // ADD/ADC/SBC A, n
            // SUB/AND/XOR/OR/CP n
            context.appendByte((byte) (this.opcode | 0b11_000_110));
            context.appendByte(valueToByte(ea.immediate, context));
            return true;
        }

        return false;
    }

}
