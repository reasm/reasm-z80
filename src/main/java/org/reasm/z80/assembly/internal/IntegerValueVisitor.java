package org.reasm.z80.assembly.internal;

import javax.annotation.Nonnull;

import org.reasm.AssemblyMessage;
import org.reasm.Function;
import org.reasm.ValueVisitor;
import org.reasm.commons.messages.FunctionCannotBeConvertedToIntegerErrorMessage;
import org.reasm.commons.messages.LossyConversionFromRealToIntegerWarningMessage;

import ca.fragag.Consumer;

abstract class IntegerValueVisitor<T> implements ValueVisitor<T> {

    @Nonnull
    final Consumer<AssemblyMessage> assemblyMessageConsumer;

    IntegerValueVisitor(@Nonnull Consumer<AssemblyMessage> assemblyMessageConsumer) {
        this.assemblyMessageConsumer = assemblyMessageConsumer;
    }

    @Override
    public T visitFloat(double value) {
        if (value != (long) value) {
            this.assemblyMessageConsumer.accept(new LossyConversionFromRealToIntegerWarningMessage(value));
        }

        return this.visitUnsignedInt((long) value);
    }

    @Override
    public T visitFunction(Function value) {
        this.assemblyMessageConsumer.accept(new FunctionCannotBeConvertedToIntegerErrorMessage());
        return this.visitUndetermined();
    }

    @Override
    public T visitSignedInt(long value) {
        return this.visitUnsignedInt(value);
    }

}
