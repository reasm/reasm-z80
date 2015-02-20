package org.reasm.z80.assembly.internal;

import java.nio.charset.Charset;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.reasm.*;
import org.reasm.commons.source.LogicalLine;
import org.reasm.commons.source.LogicalLineReader;
import org.reasm.commons.source.SourceLocationUtils;
import org.reasm.expressions.EvaluationContext;
import org.reasm.source.SourceLocation;

import ca.fragag.Consumer;

final class Z80AssemblyContext implements Consumer<AssemblyMessage>, CustomAssemblyData {

    /**
     * The key for {@link AssemblyBuilder#getCustomAssemblyData(Object)} and
     * {@link AssemblyBuilder#setCustomAssemblyData(Object, CustomAssemblyData)}.
     */
    @Nonnull
    static final Object KEY = new Object();

    /** The symbol context for mnemonics. */
    @Nonnull
    static final SymbolContext<Mnemonic> MNEMONIC = new SymbolContext<>(Mnemonic.class);

    @Nonnull
    static Z80AssemblyContext getAssemblyContext(@Nonnull AssemblyBuilder builder) {
        Z80AssemblyContext context = (Z80AssemblyContext) builder.getCustomAssemblyData(KEY);
        if (context == null) {
            // If it doesn't exist yet, create it.
            context = new Z80AssemblyContext(builder);
            builder.setCustomAssemblyData(KEY, context);
        }

        // Initialize the context.
        context.initialize(builder.getStep());
        context.encoding = builder.getAssembly().getCurrentEncoding();
        return context;
    }

    @Nonnull
    private final AssemblyBuilder builder;

    // Context of the current logical line being assembled
    // They are assigned in initialize(AssemblyStep)
    AssemblyStep step;
    long programCounter;
    SourceLocation sourceLocation;
    LogicalLine logicalLine;
    int numberOfLabels;
    int numberOfOperands;
    String mnemonic;
    @CheckForNull
    private EvaluationContext evaluationContext;

    Charset encoding;

    // Reusable objects
    @Nonnull
    final LogicalLineReader logicalLineReader = new LogicalLineReader();

    private Z80AssemblyContext(@Nonnull AssemblyBuilder builder) {
        this.builder = builder;
    }

    @Override
    public void accept(AssemblyMessage message) {
        this.builder.addTentativeMessage(message);
    }

    @Override
    public void completed() {
    }

    @Override
    public void startedNewPass() {
    }

    void addMessage(@Nonnull AssemblyMessage message) {
        this.builder.addMessage(message);
    }

    /**
     * Defines all the labels on the logical line of the current assembly step with the current program counter as their value.
     */
    void defineLabels() {
        final int numberOfLabels = this.numberOfLabels;
        for (int i = 0; i < numberOfLabels; i++) {
            this.defineLabel(i);
        }
    }

    <TValue> void defineSymbol(@Nonnull SymbolContext<TValue> symbolContext, @Nonnull String symbolName,
            @Nonnull SymbolType symbolType, @CheckForNull TValue value) {
        this.builder.defineSymbol(symbolContext, symbolName, false, symbolType, value);
    }

    @Nonnull
    EvaluationContext getEvaluationContext() {
        if (this.evaluationContext == null) {
            this.evaluationContext = new EvaluationContext(this.builder.getAssembly(), this.programCounter, this);
        }

        return this.evaluationContext;
    }

    @Nonnull
    String getLabelText(int index) {
        this.logicalLineReader.setRange(this.sourceLocation, this.logicalLine.getLabelBounds(index));
        return this.logicalLineReader.readToString();
    }

    @CheckForNull
    Symbol getMnemonicSymbolByName(@Nonnull String name) {
        return this.getSymbolByContextAndName(MNEMONIC, name, Mnemonics.SYMBOL_RESOLUTION_FALLBACK);
    }

    /**
     * Defines a label on the logical line of the current assembly step with the current program counter as its value.
     *
     * @param index
     *            the index of the label to define
     */
    private void defineLabel(int index) {
        final String label = this.getLabelText(index);
        this.defineSymbol(SymbolContext.VALUE, label, SymbolType.CONSTANT, new UnsignedIntValue(this.programCounter));
    }

    @CheckForNull
    private String getMnemonicText() {
        final SubstringBounds mnemonicBounds = this.logicalLine.getMnemonicBounds();
        if (mnemonicBounds == null) {
            return null;
        }

        this.logicalLineReader.setRange(this.sourceLocation, mnemonicBounds);
        return this.logicalLineReader.readToString();
    }

    @CheckForNull
    private <TValue> Symbol getSymbolByContextAndName(@Nonnull SymbolContext<TValue> context, @Nonnull String name,
            @Nonnull SymbolResolutionFallback symbolResolutionFallback) {
        return this.builder.resolveSymbolReference(context, name, false, false, null, symbolResolutionFallback).getSymbol();
    }

    private void initialize(@Nonnull AssemblyStep step) {
        this.step = step;
        this.programCounter = step.getProgramCounter();
        this.sourceLocation = step.getLocation().getSourceLocation();

        final LogicalLine logicalLine = SourceLocationUtils.getLogicalLine(this.sourceLocation);
        if (logicalLine != null) {
            this.logicalLine = logicalLine;
            this.numberOfLabels = this.logicalLine.getNumberOfLabels();
            this.numberOfOperands = this.logicalLine.getNumberOfOperands();
        } else {
            this.logicalLine = null;
            this.numberOfLabels = 0;
            this.numberOfOperands = 0;
        }

        this.mnemonic = this.getMnemonicText();

        // Set the evaluation context to null. It will be created on demand in getEvaluationContext().
        this.evaluationContext = null;
    }

}
