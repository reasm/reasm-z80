package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * The <code>EX</code> instruction.
 *
 * @author Francis Gagné
 */
@Immutable
class ExInstruction extends Mnemonic {

    @Nonnull
    static final ExInstruction EX = new ExInstruction();

    private ExInstruction() {
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

        switch (ea0.addressingMode) {
        case REGISTER_DE:
            if (ea1.addressingMode == AddressingMode.REGISTER_HL) {
                // EX DE, HL
                context.appendByte((byte) 0b11_101_011);
                return;
            }

            break;

        case REGISTER_AF:
            if (ea1.addressingMode == AddressingMode.REGISTER_AF_ALTERNATE) {
                // EX AF, AF'
                context.appendByte((byte) 0b00_001_000);
                return;
            }

            break;

        case REGISTER_SP_INDIRECT:
            if (ea1.addressingMode == AddressingMode.REGISTER_HL) {
                // EX (SP), HL
                context.appendByte((byte) 0b11_100_011);
                return;
            }

            if (ea1.addressingMode.isIndex()) {
                // EX (SP), IX
                // EX (SP), IY
                context.appendByte((byte) ea1.addressingMode.value);
                context.appendByte((byte) 0b11_100_011);
                return;
            }

            break;

        default:
            break;
        }

        context.addressingModeNotAllowed();
    }

}
