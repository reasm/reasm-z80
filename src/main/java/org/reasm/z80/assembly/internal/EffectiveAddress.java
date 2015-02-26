package org.reasm.z80.assembly.internal;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.reasm.AssemblyMessage;
import org.reasm.Value;
import org.reasm.commons.messages.SyntaxErrorInEffectiveAddressErrorMessage;
import org.reasm.commons.messages.ValueOutOfRangeErrorMessage;
import org.reasm.expressions.BinaryOperator;
import org.reasm.expressions.BinaryOperatorExpression;
import org.reasm.expressions.EvaluationContext;
import org.reasm.expressions.Expression;
import org.reasm.expressions.GroupingExpression;
import org.reasm.expressions.IdentifierExpression;
import org.reasm.expressions.SymbolLookup;
import org.reasm.z80.expressions.internal.ExpressionParser;
import org.reasm.z80.expressions.internal.InvalidTokenException;
import org.reasm.z80.expressions.internal.Tokenizer;

import ca.fragag.Consumer;

/**
 * Identifies the effective address of an operand.
 *
 * @author Francis Gagné
 */
final class EffectiveAddress {

    @Nonnull
    private static final Map<String, AddressingMode> REGISTER_DIRECT_NAME_TO_ADDRESSING_MODE_MAP;

    static {
        final Map<String, AddressingMode> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        map.put("B", AddressingMode.REGISTER_B);
        map.put("C", AddressingMode.REGISTER_C);
        map.put("D", AddressingMode.REGISTER_D);
        map.put("E", AddressingMode.REGISTER_E);
        map.put("H", AddressingMode.REGISTER_H);
        map.put("L", AddressingMode.REGISTER_L);
        map.put("A", AddressingMode.REGISTER_A);
        map.put("BC", AddressingMode.REGISTER_BC);
        map.put("DE", AddressingMode.REGISTER_DE);
        map.put("HL", AddressingMode.REGISTER_HL);
        map.put("SP", AddressingMode.REGISTER_SP);
        map.put("IX", AddressingMode.REGISTER_IX);
        map.put("IY", AddressingMode.REGISTER_IY);
        map.put("AF", AddressingMode.REGISTER_AF);
        map.put("AF'", AddressingMode.REGISTER_AF_ALTERNATE);
        map.put("I", AddressingMode.REGISTER_I);
        map.put("R", AddressingMode.REGISTER_R);

        REGISTER_DIRECT_NAME_TO_ADDRESSING_MODE_MAP = Collections.unmodifiableMap(map);
    }

    @Nonnull
    private static final Map<String, AddressingMode> REGISTER_INDIRECT_NAME_TO_ADDRESSING_MODE_MAP;

    static {
        final Map<String, AddressingMode> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        map.put("HL", AddressingMode.REGISTER_HL_INDIRECT);
        map.put("BC", AddressingMode.REGISTER_BC_INDIRECT);
        map.put("DE", AddressingMode.REGISTER_DE_INDIRECT);
        map.put("SP", AddressingMode.REGISTER_SP_INDIRECT);
        map.put("IX", AddressingMode.REGISTER_IX_INDIRECT);
        map.put("IY", AddressingMode.REGISTER_IY_INDIRECT);
        map.put("C", AddressingMode.REGISTER_C_INDIRECT);

        REGISTER_INDIRECT_NAME_TO_ADDRESSING_MODE_MAP = Collections.unmodifiableMap(map);
    }

    static void getEffectiveAddress(@Nonnull Tokenizer tokenizer, @CheckForNull SymbolLookup symbolLookup,
            @Nonnull EvaluationContext evaluationContext, @Nonnull Charset encoding,
            @Nonnull Consumer<AssemblyMessage> assemblyMessageConsumer, @Nonnull EffectiveAddress result) {
        // Clear the result.
        result.addressingMode = null;
        result.immediate = null;
        result.displacement = 0;

        // Parse the expression as an operand.
        final Expression expression;
        try {
            expression = ExpressionParser.parse(tokenizer, symbolLookup, assemblyMessageConsumer);
        } catch (InvalidTokenException e) {
            assemblyMessageConsumer.accept(e.createAssemblyErrorMessage());
            return;
        }

        if (expression == null) {
            assemblyMessageConsumer.accept(new SyntaxErrorInEffectiveAddressErrorMessage());
            return;
        }

        // Analyze the expression to see if it matches a register direct,
        // register indirect or indexed register addressing mode.
        if (expression instanceof IdentifierExpression) {
            final AddressingMode addressingMode = REGISTER_DIRECT_NAME_TO_ADDRESSING_MODE_MAP
                    .get(((IdentifierExpression) expression).getIdentifier());
            if (addressingMode != null) {
                result.addressingMode = addressingMode;
                return;
            }
        } else if (expression instanceof GroupingExpression) {
            final Expression childExpression = ((GroupingExpression) expression).getChildExpression();

            if (childExpression instanceof IdentifierExpression) {
                final AddressingMode addressingMode = REGISTER_INDIRECT_NAME_TO_ADDRESSING_MODE_MAP
                        .get(((IdentifierExpression) childExpression).getIdentifier());
                if (addressingMode != null) {
                    result.addressingMode = addressingMode;
                    return;
                }
            } else if (childExpression instanceof BinaryOperatorExpression) {
                final BinaryOperatorExpression binaryOperatorExpression = (BinaryOperatorExpression) childExpression;
                final BinaryOperator operator = binaryOperatorExpression.getOperator();
                final boolean subtraction = operator == BinaryOperator.SUBTRACTION;
                if (subtraction || operator == BinaryOperator.ADDITION) {
                    final Expression operand1 = binaryOperatorExpression.getOperand1();
                    if (operand1 instanceof IdentifierExpression) {
                        final String identifier = ((IdentifierExpression) operand1).getIdentifier();
                        final boolean ix = identifier.equalsIgnoreCase("IX");
                        if (ix || identifier.equalsIgnoreCase("IY")) {
                            result.addressingMode = ix ? AddressingMode.REGISTER_IX_INDEXED : AddressingMode.REGISTER_IY_INDEXED;
                            result.displacement = getDisplacement(binaryOperatorExpression.getOperand2()
                                    .evaluate(evaluationContext), subtraction, encoding, assemblyMessageConsumer);
                            return;
                        }
                    }
                }
            }

            result.addressingMode = AddressingMode.IMMEDIATE_INDIRECT;
            result.immediate = expression.evaluate(evaluationContext);
            return;
        }

        result.addressingMode = AddressingMode.IMMEDIATE;
        result.immediate = expression.evaluate(evaluationContext);
    }

    private static byte getDisplacement(@CheckForNull Value value, final boolean negate, @Nonnull final Charset encoding,
            @Nonnull Consumer<AssemblyMessage> assemblyMessageConsumer) {
        return Value.accept(value, new IntegerValueVisitor<Byte>(assemblyMessageConsumer) {
            @Override
            public Byte visitString(String value) {
                int intValue = Mnemonic.stringToInt(value, encoding, 1, this.assemblyMessageConsumer);
                if (negate) {
                    intValue = -intValue;
                }

                return (byte) intValue;
            }

            @Override
            public Byte visitUndetermined() {
                return 0;
            }

            @Override
            public Byte visitUnsignedInt(long value) {
                if (negate) {
                    value = -value;
                }

                // The displacement is applied to a 16-bit register.
                short shortValue = (short) value;

                if (shortValue < -0x80 || shortValue > 0x7F) {
                    this.assemblyMessageConsumer.accept(new ValueOutOfRangeErrorMessage(shortValue));
                }

                return (byte) shortValue;
            }
        });
    }

    AddressingMode addressingMode;
    Value immediate;
    byte displacement;

}
