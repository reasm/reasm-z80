package org.reasm.z80.assembly.internal;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;

/**
 * The <code>PUSH</code> and <code>POP</code> instructions.
 *
 * @author Francis Gagné
 */
@Immutable
class PushPopInstruction extends Mnemonic {

    @Nonnull
    static final PushPopInstruction POP = new PushPopInstruction(0b11_000_001);
    @Nonnull
    static final PushPopInstruction PUSH = new PushPopInstruction(0b11_000_101);

    @Nonnull
    private static final List<AddressingMode> REGISTER_PAIRS = ImmutableList.of(AddressingMode.REGISTER_BC,
            AddressingMode.REGISTER_DE, AddressingMode.REGISTER_HL, AddressingMode.REGISTER_AF);

    private final int opcode;

    private PushPopInstruction(int opcode) {
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

        final int registerPair = REGISTER_PAIRS.indexOf(ea.addressingMode);
        if (registerPair != -1) {
            // PUSH/POP qq
            context.appendByte((byte) (this.opcode | registerPair << 4));
            return;
        }

        if (ea.addressingMode.isIndex()) {
            // PUSH/POP IX
            // PUSH/POP IY
            context.appendByte((byte) ea.addressingMode.value);
            context.appendByte((byte) (this.opcode | 0b00_100_000));
            return;
        }

        context.addressingModeNotAllowed();
    }

}
