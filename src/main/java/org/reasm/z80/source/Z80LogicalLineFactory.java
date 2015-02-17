package org.reasm.z80.source;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.commons.source.LogicalLine;
import org.reasm.commons.source.LogicalLineAttributes;
import org.reasm.commons.source.LogicalLineFactory;

@Immutable
final class Z80LogicalLineFactory implements LogicalLineFactory {

    /** The single instance of the {@link Z80LogicalLineFactory} class. */
    @Nonnull
    static final Z80LogicalLineFactory INSTANCE = new Z80LogicalLineFactory();

    private Z80LogicalLineFactory() {
    }

    @Override
    public final LogicalLine createLogicalLine(LogicalLineAttributes attributes) {
        return new Z80LogicalLine(attributes);
    }

    @Override
    public final Class<? extends LogicalLine> getOutputType() {
        return Z80LogicalLine.class;
    }

}
