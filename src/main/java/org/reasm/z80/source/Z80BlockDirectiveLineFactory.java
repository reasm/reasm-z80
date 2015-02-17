package org.reasm.z80.source;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.commons.source.BlockDirective;
import org.reasm.commons.source.BlockDirectiveLine;
import org.reasm.commons.source.BlockDirectiveLineFactory;
import org.reasm.commons.source.LogicalLine;

@Immutable
final class Z80BlockDirectiveLineFactory implements BlockDirectiveLineFactory {

    /** The single instance of the {@link Z80BlockDirectiveLineFactory} class. */
    @Nonnull
    static final Z80BlockDirectiveLineFactory INSTANCE = new Z80BlockDirectiveLineFactory();

    private Z80BlockDirectiveLineFactory() {
    }

    @Override
    public final BlockDirectiveLine createBlockDirectiveLine(LogicalLine logicalLine, BlockDirective blockDirective) {
        return new Z80BlockDirectiveLine(logicalLine, blockDirective);
    }

    @Override
    public final Class<? extends BlockDirectiveLine> getOutputType() {
        return Z80BlockDirectiveLine.class;
    }

}
