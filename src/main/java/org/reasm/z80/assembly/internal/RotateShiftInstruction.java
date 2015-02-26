package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * The <code>RLC</code>, <code>RRC</code>, <code>RL</code>, <code>RR</code>, <code>SLA</code>, <code>SRA</code> and <code>SRL</code>
 * instructions.
 *
 * @author Francis Gagné
 */
@Immutable
class RotateShiftInstruction extends Mnemonic {

    @Nonnull
    static final RotateShiftInstruction RLC = new RotateShiftInstruction(0b00_000_000);
    @Nonnull
    static final RotateShiftInstruction RRC = new RotateShiftInstruction(0b00_001_000);
    @Nonnull
    static final RotateShiftInstruction RL = new RotateShiftInstruction(0b00_010_000);
    @Nonnull
    static final RotateShiftInstruction RR = new RotateShiftInstruction(0b00_011_000);
    @Nonnull
    static final RotateShiftInstruction SLA = new RotateShiftInstruction(0b00_100_000);
    @Nonnull
    static final RotateShiftInstruction SRA = new RotateShiftInstruction(0b00_101_000);
    @Nonnull
    static final RotateShiftInstruction SRL = new RotateShiftInstruction(0b00_111_000);

    private final int opcode;

    private RotateShiftInstruction(int opcode) {
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

        final AddressingMode addressingMode = ea.addressingMode;

        if (addressingMode.isCommon()) {
            // RLC/RRC/RL/RR/SLA/SRA/SRL r
            // RLC/RRC/RL/RR/SLA/SRA/SRL (HL)
            context.appendByte((byte) 0b11_001_011);
            context.appendByte((byte) (this.opcode | addressingMode.value));
            return;
        }

        if (addressingMode.isIndexed()) {
            // RLC/RRC/RL/RR/SLA/SRA/SRL (IX+d)
            // RLC/RRC/RL/RR/SLA/SRA/SRL (IY+d)
            context.appendByte((byte) addressingMode.value);
            context.appendByte((byte) 0b11_001_011);
            context.appendByte(ea.displacement);
            context.appendByte((byte) (this.opcode | 0b00_000_110));
            return;
        }

        context.addressingModeNotAllowed();
    }

}
