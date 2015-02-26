package org.reasm.z80.assembly.internal;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;

/**
 * The <code>ADD</code> instruction.
 *
 * @author Francis Gagné
 */
@Immutable
class AddInstruction extends Mnemonic {

    @Nonnull
    static final AddInstruction ADD = new AddInstruction();

    @Nonnull
    private static final List<AddressingMode> REGISTER_PAIRS = ImmutableList.of(AddressingMode.REGISTER_BC,
            AddressingMode.REGISTER_DE, AddressingMode.REGISTER_HL, AddressingMode.REGISTER_SP);
    @Nonnull
    private static final List<AddressingMode> REGISTER_PAIRS_IX = ImmutableList.of(AddressingMode.REGISTER_BC,
            AddressingMode.REGISTER_DE, AddressingMode.REGISTER_IX, AddressingMode.REGISTER_SP);
    @Nonnull
    private static final List<AddressingMode> REGISTER_PAIRS_IY = ImmutableList.of(AddressingMode.REGISTER_BC,
            AddressingMode.REGISTER_DE, AddressingMode.REGISTER_IY, AddressingMode.REGISTER_SP);

    private AddInstruction() {
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
            if (BinaryArithmeticLogicalInstruction.ADD.assembleCommonOrIndexedOrImmediate(context, ea1)) {
                return;
            }

            break;

        case REGISTER_HL: {
            final int registerPair = REGISTER_PAIRS.indexOf(ea1.addressingMode);
            if (registerPair != -1) {
                context.appendByte((byte) (0b00_001_001 | registerPair << 4));
                return;
            }

            break;
        }

        case REGISTER_IX: {
            final int registerPair = REGISTER_PAIRS_IX.indexOf(ea1.addressingMode);
            if (registerPair != -1) {
                context.appendByte((byte) ea0.addressingMode.value);
                context.appendByte((byte) (0b00_001_001 | registerPair << 4));
                return;
            }

            break;
        }

        case REGISTER_IY: {
            final int registerPair = REGISTER_PAIRS_IY.indexOf(ea1.addressingMode);
            if (registerPair != -1) {
                context.appendByte((byte) ea0.addressingMode.value);
                context.appendByte((byte) (0b00_001_001 | registerPair << 4));
                return;
            }

            break;
        }

        default:
            break;
        }

        context.addressingModeNotAllowed();
    }

}
