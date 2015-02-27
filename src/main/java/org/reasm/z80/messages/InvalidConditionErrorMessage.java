package org.reasm.z80.messages;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.reasm.AssemblyErrorMessage;

/**
 * An error message that is generated during an assembly when a jump, call or return instruction contains a condition operand that
 * is invalid.
 *
 * @author Francis Gagné
 */
public class InvalidConditionErrorMessage extends AssemblyErrorMessage {

    /**
     * Initializes a new InvalidConditionErrorMessage.
     *
     * @param condition
     *            the invalid condition that caused this message
     */
    public InvalidConditionErrorMessage(@Nonnull String condition) {
        super("Invalid condition: " + Objects.requireNonNull(condition, "condition"));
    }

}
