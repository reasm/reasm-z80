package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.z80.messages.InvalidConditionErrorMessage;

/**
 * The <code>CALL</code> instruction.
 *
 * @author Francis Gagné
 */
@Immutable
class CallInstruction extends Mnemonic {

    @Nonnull
    static final CallInstruction CALL = new CallInstruction();

    private CallInstruction() {
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        if (context.numberOfOperands != 1 && context.numberOfOperands != 2) {
            context.addWrongNumberOfOperandsErrorMessage();
        }

        if (context.numberOfOperands >= 2) {
            // CALL cc, nn
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

            context.appendByte((byte) (0b11_000_100 | (condition == null ? 0 : condition.ordinal() << 3)));
            context.appendWord(valueToWord(ea.immediate, context));
        } else if (context.numberOfOperands >= 1) {
            // CALL nn
            final EffectiveAddress ea = context.ea0;

            context.getEffectiveAddress(0, ea);

            if (ea.addressingMode != AddressingMode.IMMEDIATE) {
                context.addAddressingModeNotAllowedHereErrorMessage();
            }

            context.appendByte((byte) 0b11_001_101);
            context.appendWord(valueToWord(ea.immediate, context));
        } else {
            context.appendByte((byte) 0x00);
        }
    }

}
