package org.reasm.z80.source;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.commons.source.BlockDirective;
import org.reasm.commons.source.IfBlockParser;
import org.reasm.source.CompositeSourceNode;
import org.reasm.source.ParseError;
import org.reasm.source.SourceNode;

@Immutable
final class Z80IfBlockParser extends IfBlockParser {

    /** The single instance of the {@link Z80IfBlockParser} class. */
    @Nonnull
    static final Z80IfBlockParser INSTANCE = new Z80IfBlockParser();

    private Z80IfBlockParser() {
    }

    @Override
    public Iterable<Class<? extends SourceNode>> getOutputNodeTypes() {
        return BlockParsers.IF_BLOCK_TYPES;
    }

    @Override
    protected CompositeSourceNode createBlock(Iterable<? extends SourceNode> childNodes, ParseError parseError) {
        return new IfBlock(childNodes, parseError);
    }

    @Override
    protected boolean isElseDirective(BlockDirective blockDirective) {
        return blockDirective == Z80BlockDirectives.ELSE;
    }

    @Override
    protected boolean isElseIfDirective(BlockDirective blockDirective) {
        return blockDirective == Z80BlockDirectives.ELSEIF;
    }

    @Override
    protected boolean isEndIfDirective(BlockDirective blockDirective) {
        return blockDirective == Z80BlockDirectives.ENDIF;
    }

}
