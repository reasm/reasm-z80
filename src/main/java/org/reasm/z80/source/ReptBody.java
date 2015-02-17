package org.reasm.z80.source;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.AssemblyBuilder;
import org.reasm.source.CompositeSourceNode;
import org.reasm.source.SourceNode;
import org.reasm.z80.assembly.internal.SourceNodesImpl;

/**
 * The body of a <code>REPT</code> block.
 *
 * @author Francis Gagné
 */
@Immutable
public final class ReptBody extends CompositeSourceNode {

    /**
     * Initializes a new ReptBody.
     *
     * @param childNodes
     *            the child nodes
     */
    public ReptBody(@Nonnull Iterable<? extends SourceNode> childNodes) {
        super(childNodes, null);
    }

    @Override
    protected void assembleCore(AssemblyBuilder builder) throws IOException {
        SourceNodesImpl.assembleReptBody(builder);
    }

}
