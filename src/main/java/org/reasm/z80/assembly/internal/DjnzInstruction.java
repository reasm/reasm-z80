package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * The <code>DJNZ</code> instruction.
 *
 * @author Francis Gagné
 */
@Immutable
class DjnzInstruction extends Mnemonic {

    @Nonnull
    static final DjnzInstruction DJNZ = new DjnzInstruction();

    private DjnzInstruction() {
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        if (!context.requireNumberOfOperands(1)) {
            context.appendByte((byte) 0x00);
            return;
        }

        // DJNZ e
        final EffectiveAddress ea = context.ea0;

        context.getEffectiveAddress(0, ea);

        if (ea.addressingMode != AddressingMode.IMMEDIATE) {
            context.addAddressingModeNotAllowedHereErrorMessage();
        }

        context.appendByte((byte) 0b00_010_000);
        JrInstruction.appendJumpDisplacement(context, ea);
    }

}
