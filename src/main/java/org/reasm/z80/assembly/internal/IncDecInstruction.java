package org.reasm.z80.assembly.internal;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;

/**
 * The <code>INC</code> and <code>DEC</code> instructions.
 *
 * @author Francis Gagné
 */
@Immutable
class IncDecInstruction extends Mnemonic {

    @Nonnull
    static final IncDecInstruction INC = new IncDecInstruction(0b00_000_100, 0b00_000_011);
    @Nonnull
    static final IncDecInstruction DEC = new IncDecInstruction(0b00_000_101, 0b00_001_011);

    @Nonnull
    private static final List<AddressingMode> REGISTER_PAIRS = ImmutableList.of(AddressingMode.REGISTER_BC,
            AddressingMode.REGISTER_DE, AddressingMode.REGISTER_HL, AddressingMode.REGISTER_SP);

    private final int opcode8;
    private final int opcode16;

    private IncDecInstruction(int opcode8, int opcode16) {
        this.opcode8 = opcode8;
        this.opcode16 = opcode16;
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        if (!context.requireNumberOfOperands(1)) {
            context.appendByte((byte) 0x00);
            return;
        }

        final EffectiveAddress ea = context.ea0;

        context.getEffectiveAddress(0, ea);

        final AddressingMode addressingMode = ea.addressingMode;

        if (addressingMode.isCommon()) {
            // INC/DEC r
            // INC/DEC (HL)
            context.appendByte((byte) (this.opcode8 | addressingMode.value << 3));
            return;
        }

        if (addressingMode.isIndexed()) {
            // INC/DEC (IX+d)
            // INC/DEC (IY+d)
            context.appendByte((byte) addressingMode.value);
            context.appendByte((byte) (this.opcode8 | 0b00_110_000));
            context.appendByte(ea.displacement);
            return;
        }

        final int registerPair = REGISTER_PAIRS.indexOf(addressingMode);
        if (registerPair != -1) {
            // INC/DEC ss
            context.appendByte((byte) (this.opcode16 | registerPair << 4));
            return;
        }

        if (addressingMode.isIndex()) {
            // INC/DEC IX
            // INC/DEC IY
            context.appendByte((byte) addressingMode.value);
            context.appendByte((byte) (this.opcode16 | 0b00_100_000));
            return;
        }

        context.addressingModeNotAllowed();
    }

}
