package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.commons.messages.RelativeBranchTargetOutOfRangeErrorMessage;
import org.reasm.z80.messages.InvalidConditionErrorMessage;

/**
 * The <code>JR</code> instruction.
 *
 * @author Francis Gagné
 */
@Immutable
class JrInstruction extends Mnemonic {

    @Nonnull
    static final JrInstruction JR = new JrInstruction();

    static void appendJumpDisplacement(@Nonnull Z80AssemblyContext context, @Nonnull EffectiveAddress ea) throws IOException {
        final long branchTarget = valueToQword(ea.immediate, context);
        final long branchDisplacement = branchTarget - context.programCounter - 2;
        if (branchDisplacement < -0x80 || branchDisplacement > 0x7F) {
            context.addTentativeMessage(new RelativeBranchTargetOutOfRangeErrorMessage(branchDisplacement));
        }

        context.appendByte((byte) branchDisplacement);
    }

    private JrInstruction() {
    }

    @Override
    void assemble(Z80AssemblyContext context) throws IOException {
        if (context.numberOfOperands != 1 && context.numberOfOperands != 2) {
            context.addWrongNumberOfOperandsErrorMessage();
        }

        if (context.numberOfOperands >= 2) {
            // JR C, e
            // JR NC, e
            // JR Z, e
            // JR NZ, e
            final String conditionOperand = context.getOperandText(0);
            final Condition condition = Condition.parse(conditionOperand);
            if (condition == null || condition.compareTo(Condition.C) > 0) {
                context.addMessage(new InvalidConditionErrorMessage(conditionOperand));
            }

            final EffectiveAddress ea = context.ea1;

            context.getEffectiveAddress(1, ea);

            if (ea.addressingMode != AddressingMode.IMMEDIATE) {
                context.addAddressingModeNotAllowedHereErrorMessage();
            }

            context.appendByte((byte) (0b00_100_000 | (condition == null ? 0 : condition.ordinal() << 3)));
            appendJumpDisplacement(context, ea);
        } else if (context.numberOfOperands >= 1) {
            // JR e
            final EffectiveAddress ea = context.ea0;

            context.getEffectiveAddress(0, ea);

            if (ea.addressingMode != AddressingMode.IMMEDIATE) {
                context.addAddressingModeNotAllowedHereErrorMessage();
            }

            context.appendByte((byte) 0b00_011_000);
            appendJumpDisplacement(context, ea);
        } else {
            context.appendByte((byte) 0x00);
        }
    }

}
