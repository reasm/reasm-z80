package org.reasm.z80.assembly.internal;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.commons.messages.AddressingModeNotAllowedHereErrorMessage;

import com.google.common.collect.ImmutableList;

/**
 * The <code>ADC</code> and <code>SBC</code> instructions.
 *
 * @author Francis Gagné
 */
@Immutable
class AdcSbcInstruction extends Mnemonic {

    @Nonnull
    static final AdcSbcInstruction ADC = new AdcSbcInstruction(BinaryArithmeticLogicalInstruction.ADC, 0b01_001_010);
    @Nonnull
    static final AdcSbcInstruction SBC = new AdcSbcInstruction(BinaryArithmeticLogicalInstruction.SBC, 0b01_000_010);

    @Nonnull
    private static final List<AddressingMode> REGISTER_PAIRS = ImmutableList.of(AddressingMode.REGISTER_BC,
            AddressingMode.REGISTER_DE, AddressingMode.REGISTER_HL, AddressingMode.REGISTER_SP);

    @Nonnull
    private final BinaryArithmeticLogicalInstruction opcodesToA;
    private final int opcodeToHL;

    private AdcSbcInstruction(@Nonnull BinaryArithmeticLogicalInstruction opcodesToA, int opcodeToHL) {
        this.opcodesToA = opcodesToA;
        this.opcodeToHL = opcodeToHL;
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
        case REGISTER_A:
            if (this.opcodesToA.assembleCommonOrIndexedOrImmediate(context, ea1)) {
                return;
            }

            break;

        case REGISTER_HL: {
            final int registerPair = REGISTER_PAIRS.indexOf(ea1.addressingMode);
            if (registerPair != -1) {
                context.appendByte((byte) 0b11_101_101);
                context.appendByte((byte) (0b01_000_010 | this.opcodeToHL | registerPair << 4));
                return;
            }

            break;
        }

        default:
            break;
        }

        context.appendByte((byte) 0x00);
        context.addMessage(new AddressingModeNotAllowedHereErrorMessage());
    }

}
