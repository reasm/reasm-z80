package org.reasm.z80.source;

import java.io.IOException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.AssemblyBuilder;
import org.reasm.source.CompositeSourceNode;
import org.reasm.source.ParseError;
import org.reasm.source.SourceNode;
import org.reasm.z80.assembly.internal.SourceNodesImpl;

/**
 * A <code>PHASE</code> block.
 *
 * @author Francis Gagné
 */
@Immutable
public final class PhaseBlock extends CompositeSourceNode {

    /**
     * Initializes a new PhaseBlock.
     *
     * @param childNodes
     *            the child nodes
     * @param parseError
     *            the parse error on the source node, or <code>null</code> if no parse error occurred
     */
    public PhaseBlock(@Nonnull Iterable<? extends SourceNode> childNodes, @CheckForNull ParseError parseError) {
        super(childNodes, parseError);
    }

    @Override
    protected void assembleCore(AssemblyBuilder builder) throws IOException {
        SourceNodesImpl.assemblePhaseBlock(builder);
    }

}
