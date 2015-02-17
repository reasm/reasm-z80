package org.reasm.z80.source;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.AssemblyBuilder;
import org.reasm.commons.source.BlockDirective;
import org.reasm.commons.source.BlockDirectiveLine;
import org.reasm.commons.source.LogicalLine;
import org.reasm.z80.assembly.internal.SourceNodesImpl;

/**
 * Wraps a {@link LogicalLine} whose mnemonic is a directive that delimits a block in order to bypass macro lookup.
 *
 * @author Francis Gagné
 */
@Immutable
public final class Z80BlockDirectiveLine extends BlockDirectiveLine {

    Z80BlockDirectiveLine(@Nonnull LogicalLine logicalLine, @Nonnull BlockDirective blockDirective) {
        super(logicalLine, blockDirective);
    }

    @Override
    protected void assembleCore(AssemblyBuilder builder) throws IOException {
        SourceNodesImpl.assembleBlockDirectiveLine(builder);
    }

}
