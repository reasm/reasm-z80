package org.reasm.z80.assembly.internal;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.AssemblyMessage;
import org.reasm.Value;
import org.reasm.commons.messages.StringTooLongErrorMessage;
import org.reasm.commons.messages.ValueOutOfRangeErrorMessage;

import ca.fragag.Consumer;

/**
 * Base class for all instructions and directives provided by the Z80 architecture family.
 *
 * @author Francis Gagné
 */
@Immutable
abstract class Mnemonic {

    static int stringToInt(@Nonnull String value, @Nonnull Charset encoding, int maxLength,
            @Nonnull Consumer<AssemblyMessage> assemblyMessageConsumer) {
        final ByteBuffer stringBytes = encoding.encode(value);

        if (stringBytes.limit() > maxLength) {
            assemblyMessageConsumer.accept(new StringTooLongErrorMessage(value));
        }

        if (maxLength > stringBytes.limit()) {
            maxLength = stringBytes.limit();
        }

        int result = 0;
        for (; maxLength != 0; maxLength--) {
            result <<= 8;
            result |= stringBytes.get() & 0xFF;
        }

        return result;
    }

    static byte valueToByte(@CheckForNull Value value, @Nonnull final Z80AssemblyContext context) {
        return Value.accept(value, new IntegerValueVisitor<Byte>(context) {
            @Override
            public Byte visitString(String value) {
                return (byte) stringToInt(value, context.encoding, 1, this.assemblyMessageConsumer);
            }

            @Override
            public Byte visitUnsignedInt(long value) {
                if (value < -0x80 || value > 0xFF) {
                    this.assemblyMessageConsumer.accept(new ValueOutOfRangeErrorMessage(value));
                }

                return (byte) value;
            }
        });
    }

    static short valueToWord(@Nonnull Value value, @Nonnull final Z80AssemblyContext context) {
        return Value.accept(value, new IntegerValueVisitor<Short>(context) {
            @Override
            public Short visitString(String value) {
                return (short) stringToInt(value, context.encoding, 2, this.assemblyMessageConsumer);
            }

            @Override
            public Short visitUnsignedInt(long value) {
                if (value < -0x8000 || value > 0xFFFF) {
                    this.assemblyMessageConsumer.accept(new ValueOutOfRangeErrorMessage(value));
                }

                return (short) value;
            }
        });
    }

    /**
     * Assembles the directive or instruction on the logical line of the context's current assembly step.
     *
     * @param context
     *            the assembly context
     * @throws IOException
     *             an I/O exception occurred
     */
    abstract void assemble(@Nonnull Z80AssemblyContext context) throws IOException;

    void defineLabels(@Nonnull Z80AssemblyContext context) {
        context.defineLabels();
    }

}
