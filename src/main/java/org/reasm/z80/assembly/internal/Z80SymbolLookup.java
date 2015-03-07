package org.reasm.z80.assembly.internal;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.Symbol;
import org.reasm.SymbolContext;
import org.reasm.SymbolLookupContext;
import org.reasm.expressions.SymbolLookup;

/**
 * Provides symbol lookup in a particular {@link SymbolLookupContext}.
 *
 * @author Francis Gagné
 */
@Immutable
final class Z80SymbolLookup implements SymbolLookup {

    @Nonnull
    private final Z80AssemblyContext context;
    @Nonnull
    private final SymbolLookupContext symbolLookupContext;

    Z80SymbolLookup(@Nonnull Z80AssemblyContext context, @Nonnull SymbolLookupContext symbolLookupContext) {
        this.context = context;
        this.symbolLookupContext = symbolLookupContext;
    }

    @Override
    public final boolean equals(@CheckForNull Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        final Z80SymbolLookup other = (Z80SymbolLookup) obj;
        if (!this.context.equals(other.context)) {
            return false;
        }

        if (!this.symbolLookupContext.equals(other.symbolLookupContext)) {
            return false;
        }

        return true;
    }

    @Override
    public final Symbol getSymbol(String name) {
        return this.context.builder
                .resolveSymbolReference(SymbolContext.VALUE, name, false, this.symbolLookupContext, this.context).getSymbol();
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.context.hashCode();
        result = prime * result + this.symbolLookupContext.hashCode();
        return result;
    }

}
