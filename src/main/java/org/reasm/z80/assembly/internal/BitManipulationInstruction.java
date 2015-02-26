package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.commons.messages.ValueOutOfRangeErrorMessage;

/**
 * The <code>BIT</code>, <code>RES</code> and <code>SET</code> instructions.
 *
 * @author Francis Gagné
 */
@Immutable
class BitManipulationInstruction extends Mnemonic {

    @Nonnull
    static final BitManipulationInstruction BIT = new BitManipulationInstruction(0b01_000_000);
    @Nonnull
    static final BitManipulationInstruction RES = new BitManipulationInstruction(0b10_000_000);
    @Nonnull
    static final BitManipulationInstruction SET = new BitManipulationInstruction(0b11_000_000);

    private final int opcode;

    private BitManipulationInstruction(int opcode) {
        this.opcode = opcode;
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        if (!context.requireNumberOfOperands(2)) {
            context.appendByte((byte) 0x00);
            return;
        }

        final EffectiveAddress ea0 = context.ea0;
        final EffectiveAddress ea1 = context.ea1;

        context.getEffectiveAddress(0, ea0);
        context.getEffectiveAddress(1, ea1);

        if (ea0.addressingMode == AddressingMode.IMMEDIATE) {
            final long bitNumber = valueToQword(ea0.immediate, context);
            if (bitNumber < 0 || bitNumber > 7) {
                context.addTentativeMessage(new ValueOutOfRangeErrorMessage(bitNumber));
            }

            final long sanitizedBitNumber = bitNumber & 7;

            final AddressingMode addressingMode = ea1.addressingMode;

            if (addressingMode.isCommon()) {
                // BIT/RES/SET r
                // BIT/RES/SET (HL)
                context.appendByte((byte) 0b11_001_011);
                context.appendByte((byte) (this.opcode | sanitizedBitNumber << 3 | addressingMode.value));
                return;
            }

            if (addressingMode.isIndexed()) {
                // BIT/RES/SET (IX+d)
                // BIT/RES/SET (IY+d)
                context.appendByte((byte) addressingMode.value);
                context.appendByte((byte) 0b11_001_011);
                context.appendByte(ea1.displacement);
                context.appendByte((byte) (this.opcode | sanitizedBitNumber << 3 | 0b00_000_110));
                return;
            }
        }

        context.addressingModeNotAllowed();
    }

}
