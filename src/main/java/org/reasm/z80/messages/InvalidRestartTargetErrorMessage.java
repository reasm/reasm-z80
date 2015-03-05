package org.reasm.z80.messages;

import org.reasm.AssemblyErrorMessage;

/**
 * An error message that is generated during an assembly when the target operand of a <code>RST</code> instruction is not one of the
 * 8 possible values (0, 8, 16, 24, 32, 40, 48 or 56).
 *
 * @author Francis Gagné
 */
public class InvalidRestartTargetErrorMessage extends AssemblyErrorMessage {

    private final long target;

    /**
     * Initializes a new InvalidRestartTargetErrorMessage.
     *
     * @param target
     *            the invalid target
     */
    public InvalidRestartTargetErrorMessage(long target) {
        super("Invalid target for RST instruction (the address must be 0, 8, 16, 24, 32, 40, 48 or 56, but was " + target + ")");
        this.target = target;
    }

    /**
     * Gets the invalid target address that caused this error message.
     *
     * @return the invalid target
     */
    public final long getTarget() {
        return this.target;
    }

}
