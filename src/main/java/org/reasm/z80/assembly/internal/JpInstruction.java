package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.z80.messages.InvalidConditionErrorMessage;

/**
 * The <code>JP</code> instruction.
 *
 * @author Francis Gagné
 */
@Immutable
class JpInstruction extends Mnemonic {

    @Nonnull
    static final JpInstruction JP = new JpInstruction();

    private JpInstruction() {
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        if (context.numberOfOperands != 1 && context.numberOfOperands != 2) {
            context.addWrongNumberOfOperandsErrorMessage();
        }

        if (context.numberOfOperands >= 2) {
            // JP cc, nn
            final String conditionOperand = context.getOperandText(0);
            final Condition condition = Condition.parse(conditionOperand);
            if (condition == null) {
                context.addMessage(new InvalidConditionErrorMessage(conditionOperand));
            }

            final EffectiveAddress ea = context.ea1;

            context.getEffectiveAddress(1, ea);

            if (ea.addressingMode != AddressingMode.IMMEDIATE) {
                context.addAddressingModeNotAllowedHereErrorMessage();
            }

            context.appendByte((byte) (0b11_000_010 | (condition == null ? 0 : condition.ordinal() << 3)));
            context.appendWord(valueToWord(ea.immediate, context));
        } else if (context.numberOfOperands >= 1) {
            final EffectiveAddress ea = context.ea0;

            context.getEffectiveAddress(0, ea);

            switch (ea.addressingMode) {
            case IMMEDIATE:
                // JP nn
                context.appendByte((byte) 0b11_000_011);
                context.appendWord(valueToWord(ea.immediate, context));
                return;

            case REGISTER_HL_INDIRECT:
                // JP (HL)
                context.appendByte((byte) 0b11_101_001);
                return;

            case REGISTER_IX_INDIRECT:
            case REGISTER_IY_INDIRECT:
                // JP (IX)
                // JP (IY)
                context.appendByte((byte) ea.addressingMode.value);
                context.appendByte((byte) 0b11_101_001);
                return;

            default:
                break;
            }

            context.addressingModeNotAllowed();
        } else {
            context.appendByte((byte) 0x00);
        }
    }

}
