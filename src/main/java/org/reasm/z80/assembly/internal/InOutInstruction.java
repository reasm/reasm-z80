package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * The <code>IN</code> and <code>OUT</code> instructions.
 *
 * @author Francis Gagné
 */
@Immutable
class InOutInstruction extends Mnemonic {

    @Nonnull
    static final InOutInstruction IN = new InOutInstruction(0b11_011_011, 0b01_000_000, 0, 1);
    @Nonnull
    static final InOutInstruction OUT = new InOutInstruction(0b11_010_011, 0b01_000_001, 1, 0);

    private final int fixedPortOpcode;
    private final int variablePortOpcode;
    private final int registerOperandIndex;
    private final int portOperandIndex;

    private InOutInstruction(int fixedPortOpcode, int variablePortOpcode, int registerOperandIndex, int portOperandIndex) {
        this.fixedPortOpcode = fixedPortOpcode;
        this.variablePortOpcode = variablePortOpcode;
        this.registerOperandIndex = registerOperandIndex;
        this.portOperandIndex = portOperandIndex;
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        if (!context.requireNumberOfOperands(2)) {
            context.appendByte((byte) 0x00);
            return;
        }

        final EffectiveAddress ea0 = context.ea0;
        final EffectiveAddress ea1 = context.ea1;

        context.getEffectiveAddress(this.registerOperandIndex, ea0);
        context.getEffectiveAddress(this.portOperandIndex, ea1);

        if (ea0.addressingMode == AddressingMode.REGISTER_A && ea1.addressingMode == AddressingMode.IMMEDIATE_INDIRECT) {
            // IN A, (n)
            // OUT (n), A
            context.appendByte((byte) this.fixedPortOpcode);
            context.appendByte(valueToByte(ea1.immediate, context));
            return;
        }

        if (ea0.addressingMode.isCommon() && ea1.addressingMode == AddressingMode.REGISTER_C_INDIRECT) {
            // IN r, (C)
            // OUT (C), r
            context.appendByte((byte) 0b11_101_101);
            context.appendByte((byte) (this.variablePortOpcode | ea0.addressingMode.value << 3));

            if (ea0.addressingMode == AddressingMode.REGISTER_HL_INDIRECT) {
                context.addAddressingModeNotAllowedHereErrorMessage();
            }

            return;
        }

        context.addressingModeNotAllowed();
    }

}
