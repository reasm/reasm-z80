package org.reasm.z80.source;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.AssemblyBuilder;
import org.reasm.commons.source.LogicalLine;
import org.reasm.commons.source.LogicalLineAttributes;
import org.reasm.z80.assembly.internal.SourceNodesImpl;

/**
 * A logical line in a Zilog Z80 family assembly source file.
 *
 * @author Francis Gagné
 */
@Immutable
public final class Z80LogicalLine extends LogicalLine {

    Z80LogicalLine(@Nonnull LogicalLineAttributes attributes) {
        super(attributes);
    }

    @Override
    protected void assembleCore(AssemblyBuilder builder) throws IOException {
        SourceNodesImpl.assembleLogicalLine(builder);
    }

}
