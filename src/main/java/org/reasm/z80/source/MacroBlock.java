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
 * A <code>MACRO</code> block.
 *
 * @author Francis Gagné
 */
@Immutable
public final class MacroBlock extends CompositeSourceNode {

    /**
     * Initializes a new MacroBlock.
     *
     * @param childNodes
     *            the child nodes
     * @param parseError
     *            the parse error on the source node, or <code>null</code> if no parse error occurred
     */
    public MacroBlock(@Nonnull Iterable<? extends SourceNode> childNodes, @CheckForNull ParseError parseError) {
        super(childNodes, parseError);
    }

    @Override
    protected void assembleCore(AssemblyBuilder builder) throws IOException {
        SourceNodesImpl.assembleMacroBlock(builder);
    }

}
