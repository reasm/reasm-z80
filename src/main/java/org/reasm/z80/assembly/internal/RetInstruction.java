package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.z80.messages.InvalidConditionErrorMessage;

/**
 * The <code>RET</code> instruction.
 *
 * @author Francis Gagné
 */
@Immutable
class RetInstruction extends Mnemonic {

    @Nonnull
    static final RetInstruction RET = new RetInstruction();

    private RetInstruction() {
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        if (context.numberOfOperands != 0 && context.numberOfOperands != 1) {
            context.addWrongNumberOfOperandsErrorMessage();
        }

        if (context.numberOfOperands >= 1) {
            // RET cc
            final String conditionOperand = context.getOperandText(0);
            final Condition condition = Condition.parse(conditionOperand);
            if (condition == null) {
                context.addMessage(new InvalidConditionErrorMessage(conditionOperand));
            }

            context.appendByte((byte) (0b11_000_000 | (condition == null ? 0 : condition.ordinal() << 3)));
        } else {
            // RET
            context.appendByte((byte) 0b11_001_001);
        }
    }

}
