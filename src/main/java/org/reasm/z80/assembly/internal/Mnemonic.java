package org.reasm.z80.assembly.internal;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * Base class for all instructions and directives provided by the Z80 architecture family.
 *
 * @author Francis Gagné
 */
@Immutable
abstract class Mnemonic {

    /**
     * Assembles the directive or instruction on the logical line of the context's current assembly step.
     *
     * @param context
     *            the assembly context
     * @throws IOException
     *             an I/O exception occurred
     */
    abstract void assemble(@Nonnull Z80AssemblyContext context) throws IOException;

    void defineLabels(@Nonnull Z80AssemblyContext context) {
        context.defineLabels();
    }

}
