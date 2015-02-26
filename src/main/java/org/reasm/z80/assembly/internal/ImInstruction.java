package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.commons.messages.AddressingModeNotAllowedHereErrorMessage;
import org.reasm.z80.messages.InvalidImmediateModeErrorMessage;

/**
 * The <code>IM</code> instruction.
 *
 * @author Francis Gagné
 */
@Immutable
class ImInstruction extends Mnemonic {

    @Nonnull
    static final ImInstruction IM = new ImInstruction();

    private ImInstruction() {
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        if (!context.requireNumberOfOperands(1)) {
            context.appendByte((byte) 0x00);
            return;
        }

        final EffectiveAddress ea = context.ea0;

        context.getEffectiveAddress(0, ea);

        if (ea.addressingMode == AddressingMode.IMMEDIATE) {
            final long immediateMode = valueToQword(ea.immediate, context);
            final byte opcode;
            if (immediateMode == 0) {
                // IM 0
                opcode = 0b01_000_110;
            } else if (immediateMode == 1) {
                // IM 1
                opcode = 0b01_010_110;
            } else if (immediateMode == 2) {
                // IM 2
                opcode = 0b01_011_110;
            } else {
                opcode = 0b01_000_110;
                context.addTentativeMessage(new InvalidImmediateModeErrorMessage());
            }

            context.appendByte((byte) 0b11_101_101);
            context.appendByte(opcode);
            return;
        }

        context.appendByte((byte) 0x00);
        context.addMessage(new AddressingModeNotAllowedHereErrorMessage());
    }
}
