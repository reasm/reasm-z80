package org.reasm.z80.messages;

import org.reasm.AssemblyErrorMessage;

/**
 * An error message that is generated during an assembly when the operand to an IM instruction is not a valid immediate mode (either
 * 0, 1 or 2).
 *
 * @author Francis Gagné
 */
public class InvalidImmediateModeErrorMessage extends AssemblyErrorMessage {

    /**
     * Initializes a new InvalidImmediateModeErrorMessage.
     */
    public InvalidImmediateModeErrorMessage() {
        super("Operand to IM must be either 0, 1 or 2");
    }

}
