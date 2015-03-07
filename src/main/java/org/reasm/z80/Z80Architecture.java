package org.reasm.z80;

import java.util.Arrays;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.Architecture;
import org.reasm.Assembly;
import org.reasm.AssemblyMessage;
import org.reasm.Symbol;
import org.reasm.SymbolContext;
import org.reasm.SymbolReference;
import org.reasm.Value;
import org.reasm.expressions.EvaluationContext;
import org.reasm.expressions.SymbolLookup;
import org.reasm.source.AbstractSourceFile;
import org.reasm.source.SourceNode;
import org.reasm.z80.source.Z80Parser;

import ca.fragag.Consumer;
import ca.fragag.text.Document;

/**
 * The Zilog Z80 architecture.
 *
 * @author Francis Gagné
 */
@Immutable
public final class Z80Architecture extends Architecture {

    /** The Z80 architecture. */
    @Nonnull
    public static final Z80Architecture INSTANCE = new Z80Architecture();

    /**
     * Initializes a new Z80Architecture.
     */
    private Z80Architecture() {
        super(Arrays.asList("Z80"));
    }

    @CheckForNull
    @Override
    public final Value evaluateExpression(@Nonnull CharSequence expression, @Nonnull final Assembly assembly,
            @CheckForNull final Consumer<SymbolReference> symbolReferenceConsumer,
            @CheckForNull Consumer<AssemblyMessage> assemblyMessageConsumer) {
        if (expression == null) {
            throw new NullPointerException("expression");
        }

        if (assembly == null) {
            throw new NullPointerException("assembly");
        }

        final SymbolLookup symbolLookup = new SymbolLookup() {
            @Override
            public Symbol getSymbol(String name) {
                final SymbolReference symbolReference = assembly.resolveSymbolReference(SymbolContext.VALUE, name, false, null,
                        null);

                if (symbolReferenceConsumer != null) {
                    symbolReferenceConsumer.accept(symbolReference);
                }

                return symbolReference.getSymbol();
            }
        };

        final EvaluationContext evaluationContext = new EvaluationContext(assembly, assembly.getProgramCounter(),
                assemblyMessageConsumer);
        return Expressions.parse(expression, symbolLookup, assemblyMessageConsumer).evaluate(evaluationContext);
    }

    @Nonnull
    @Override
    public final SourceNode parse(@Nonnull Document text) {
        return Z80Parser.INSTANCE.parse(text);
    }

    @Nonnull
    @Override
    public final SourceNode reparse(@Nonnull Document text, @Nonnull AbstractSourceFile<?> oldSourceFile, int replaceOffset,
            int lengthToRemove, int lengthToInsert) {
        return Z80Parser.INSTANCE.reparse(text, oldSourceFile.getParsed(this), replaceOffset, lengthToRemove, lengthToInsert);
    }

}
