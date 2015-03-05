package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.z80.messages.InvalidRestartTargetErrorMessage;

/**
 * The <code>RST</code> instruction.
 *
 * @author Francis Gagné
 */
@Immutable
class RstInstruction extends Mnemonic {

    @Nonnull
    static final RstInstruction RST = new RstInstruction();

    private RstInstruction() {
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        if (!context.requireNumberOfOperands(1)) {
            context.appendByte((byte) 0x00);
            return;
        }

        // RST p
        final EffectiveAddress ea = context.ea0;

        context.getEffectiveAddress(0, ea);

        if (ea.addressingMode != AddressingMode.IMMEDIATE) {
            context.addAddressingModeNotAllowedHereErrorMessage();
        }

        final long target = valueToQword(ea.immediate, context);
        if ((target & ~0x38) != 0) {
            context.addTentativeMessage(new InvalidRestartTargetErrorMessage(target));
        }

        context.appendByte((byte) (0b11_000_111 | target & 0x38));
    }
}
