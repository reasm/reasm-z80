package org.reasm.z80.expressions.internal;

import java.util.ArrayList;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.reasm.AssemblyMessage;
import org.reasm.FloatValue;
import org.reasm.StringValue;
import org.reasm.UnsignedIntValue;
import org.reasm.commons.expressions.Expressions;
import org.reasm.commons.source.Syntax;
import org.reasm.expressions.*;
import org.reasm.messages.OverflowInLiteralWarningMessage;

import ca.fragag.Consumer;

import com.google.common.primitives.UnsignedLongs;

/**
 * Contains a method to parse an expression from a sequence of tokens.
 *
 * @author Francis Gagné
 */
public final class ExpressionParser {

    @Nonnull
    private static final Expression[] NO_ARGUMENTS = new Expression[0];

    // The SymbolLookup of this IdentifierExpression is irrelevant, since that expression is never evaluated.
    @Nonnull
    private static final IdentifierExpression EMPTY_IDENTIFIER = new IdentifierExpression("", null);

    /**
     * Parses an expression from the tokens emitted by the specified tokenizer.
     *
     * @param tokenizer
     *            the tokenizer to read tokens from
     * @param symbolLookup
     *            an object that looks up symbols by name, which will be used to look up the symbol for identifiers when the
     *            identifier is {@linkplain IdentifierExpression#evaluate(EvaluationContext) evaluated}, or <code>null</code> to
     *            consider all identifiers undefined
     * @param assemblyMessageConsumer
     *            a {@link Consumer} that will receive {@link AssemblyMessage}s generated while parsing the expression
     * @return the parsed {@link Expression}, or <code>null</code> if an expression could not be parsed
     * @throws InvalidTokenException
     *             an {@linkplain TokenType#INVALID invalid} token was emitted by the tokenizer
     */
    @CheckForNull
    public static Expression parse(@Nonnull Tokenizer tokenizer, @CheckForNull SymbolLookup symbolLookup,
            @CheckForNull Consumer<AssemblyMessage> assemblyMessageConsumer) throws InvalidTokenException {
        Expression expression = parseLevel3(tokenizer, symbolLookup, assemblyMessageConsumer);
        if (expression != null) {
            Tokenizer tokenizer1;
            for (;; tokenizer.copyFrom(tokenizer1)) {
                if (tokenizer.getTokenType() != TokenType.CONDITIONAL_OPERATOR_FIRST) {
                    break;
                }

                tokenizer1 = tokenizer.duplicateAndAdvance();
                Expression truePart = parse(tokenizer1, symbolLookup, assemblyMessageConsumer);
                if (truePart == null) {
                    break;
                }

                if (tokenizer1.getTokenType() != TokenType.CONDITIONAL_OPERATOR_SECOND) {
                    break;
                }

                tokenizer1.advance();
                Expression falsePart = parse(tokenizer1, symbolLookup, assemblyMessageConsumer);
                if (falsePart == null) {
                    break;
                }

                expression = new ConditionalExpression(expression, truePart, falsePart);
            }
        }

        return expression;
    }

    @CheckForNull
    private static Z80BinaryOperator parseBinaryOperator(@Nonnull Tokenizer tokenizer) {
        if (tokenizer.getTokenType() != TokenType.OPERATOR) {
            return null;
        }

        // Check for characters that only correspond to single-character operators.
        final char ch1 = tokenizer.tokenCharAt(0);
        switch (ch1) {
        case '*':
            return Z80BinaryOperator.MULTIPLICATION;

        case '/':
            return Z80BinaryOperator.DIVISION;

        case '%':
            return Z80BinaryOperator.MODULUS;

        case '+':
            return Z80BinaryOperator.ADDITION;

        case '-':
            return Z80BinaryOperator.SUBTRACTION;

        case '^':
            return Z80BinaryOperator.BITWISE_XOR;

        default:
            break;
        }

        // Check for characters that appear as the first character of a 2-character operator.
        final int ch2 = tokenizer.getTokenLength() > 1 ? tokenizer.tokenCharAt(1) : -1;

        switch (ch1) {
        case '<':
            switch (ch2) {
            case -1:
                return Z80BinaryOperator.LESS_THAN;

            case '<':
                return Z80BinaryOperator.BIT_SHIFT_LEFT;

            case '=':
                return Z80BinaryOperator.LESS_THAN_OR_EQUAL_TO;

            case '>':
                return Z80BinaryOperator.DIFFERENT_FROM;

            default:
                throw new AssertionError(); // unreachable
            }

        case '>':
            switch (ch2) {
            case -1:
                return Z80BinaryOperator.GREATER_THAN;

            case '=':
                return Z80BinaryOperator.GREATER_THAN_OR_EQUAL_TO;

            case '>':
                return Z80BinaryOperator.BIT_SHIFT_RIGHT;

            default:
                throw new AssertionError(); // unreachable
            }

        case '=':
            switch (ch2) {
            case -1:
                return Z80BinaryOperator.EQUAL_TO;

            case '=':
                return Z80BinaryOperator.STRICTLY_EQUAL_TO;

            default:
                throw new AssertionError(); // unreachable
            }

        case '&':
            switch (ch2) {
            case -1:
                return Z80BinaryOperator.BITWISE_AND;

            case '&':
                return Z80BinaryOperator.LOGICAL_AND;

            default:
                throw new AssertionError(); // unreachable
            }

        case '|':
            switch (ch2) {
            case -1:
                return Z80BinaryOperator.BITWISE_OR;

            case '|':
                return Z80BinaryOperator.LOGICAL_OR;

            default:
                throw new AssertionError(); // unreachable
            }

        case '!':
            switch (ch2) {
            case -1:
                return null; // not a binary operator

            case '=':
                return Z80BinaryOperator.STRICTLY_DIFFERENT_FROM;

            default:
                throw new AssertionError(); // unreachable
            }

        default:
            return null;
        }
    }

    @CheckForNull
    private static Expression parseLevel0(@Nonnull Tokenizer tokenizer, @CheckForNull SymbolLookup symbolLookup,
            @CheckForNull Consumer<AssemblyMessage> assemblyMessageConsumer) throws InvalidTokenException {
        final CharSequence tokenText = tokenizer.getTokenText();
        Expression expression = null;
        switch (tokenizer.getTokenType()) {
        case INVALID:
            throw new InvalidTokenException(tokenText.toString());

        case DECIMAL_INTEGER: {
            final long intValue = parseUnsignedLongWithOverflow(tokenText, 10, assemblyMessageConsumer);
            expression = new ValueExpression(new UnsignedIntValue(intValue));
            break;
        }

        case BINARY_INTEGER: {
            final long intValue = parseUnsignedLongWithOverflow(tokenText.subSequence(0, tokenizer.getTokenLength() - 1), 2,
                    assemblyMessageConsumer);
            expression = new ValueExpression(new UnsignedIntValue(intValue));
            break;
        }

        case HEXADECIMAL_INTEGER: {
            final long intValue = parseUnsignedLongWithOverflow(tokenText.subSequence(0, tokenizer.getTokenLength() - 1), 16,
                    assemblyMessageConsumer);
            expression = new ValueExpression(new UnsignedIntValue(intValue));
            break;
        }

        case REAL:
            final double floatValue = Expression.parseFloatWithOverflow(tokenText);
            expression = new ValueExpression(new FloatValue(floatValue));
            break;

        case STRING:
            final String stringValue = Expressions.parseString(tokenText, assemblyMessageConsumer);
            expression = new ValueExpression(new StringValue(stringValue));
            break;

        case IDENTIFIER:
            final String identifier = tokenText.toString();
            expression = new IdentifierExpression(identifier, symbolLookup);
            break;

        case OPERATOR:
            if (tokenizer.tokenEqualsString("*")) {
                expression = ProgramCounterExpression.INSTANCE;
            }

            break;

        case OPENING_PARENTHESIS:
            Tokenizer tokenizer1 = tokenizer.duplicateAndAdvance();
            Expression childExpression = parse(tokenizer1, symbolLookup, assemblyMessageConsumer);
            if (childExpression == null) {
                return null;
            }

            if (tokenizer1.getTokenType() != TokenType.CLOSING_PARENTHESIS) {
                return null;
            }

            tokenizer.copyFrom(tokenizer1);
            expression = new GroupingExpression(childExpression);
            break;

        default:
            break;
        }

        if (expression != null) {
            tokenizer.advance();
        }

        return expression;
    }

    @CheckForNull
    private static Expression parseLevel1(@Nonnull Tokenizer tokenizer, @CheckForNull SymbolLookup symbolLookup,
            @CheckForNull Consumer<AssemblyMessage> assemblyMessageConsumer) throws InvalidTokenException {
        if (tokenizer.getTokenType() == TokenType.PLUS_OR_MINUS_SEQUENCE) {
            tokenizer.breakSequence();
        }

        if (tokenizer.getTokenType() == TokenType.PERIOD) {
            // Treat the period operator as a unary operator too.
            final Tokenizer tokenizer1 = tokenizer.duplicateAndAdvance();
            final Expression expression1 = parseLevel1(tokenizer1, symbolLookup, assemblyMessageConsumer);
            if (expression1 != null) {
                tokenizer.copyFrom(tokenizer1);
                return new PeriodExpression(EMPTY_IDENTIFIER, expression1, symbolLookup);
            }

            return null;
        }

        if (tokenizer.getTokenType() == TokenType.OPERATOR && tokenizer.getTokenLength() == 1) {
            UnaryOperator operator = null;
            // Check if the operator is a unary operator.
            switch (tokenizer.tokenCharAt(0)) {
            case '!':
                operator = UnaryOperator.LOGICAL_NOT;
                break;

            case '+':
                operator = UnaryOperator.UNARY_PLUS;
                break;

            case '-':
                operator = UnaryOperator.NEGATION;
                break;

            case '~':
                operator = UnaryOperator.BITWISE_NOT;
                break;
            }

            if (operator != null) {
                final Tokenizer tokenizer1 = tokenizer.duplicateAndAdvance();
                final Expression expression1 = parseLevel1(tokenizer1, symbolLookup, assemblyMessageConsumer);
                if (expression1 != null) {
                    tokenizer.copyFrom(tokenizer1);
                    return new UnaryOperatorExpression(operator, expression1);
                }

                return null;
            }
        }

        Expression expression = parseLevel0(tokenizer, symbolLookup, assemblyMessageConsumer);
        if (expression != null) {
            Tokenizer tokenizer1;
            outer: for (;; tokenizer.copyFrom(tokenizer1)) {
                final TokenType tokenType = tokenizer.getTokenType();
                switch (tokenType) {
                case OPENING_PARENTHESIS:
                    tokenizer1 = tokenizer.duplicateAndAdvance();

                    // If the argument list is empty, return a function call expression with no arguments.
                    if (tokenizer1.getTokenType() == TokenType.CLOSING_PARENTHESIS) {
                        tokenizer1.advance();
                        expression = new FunctionCallExpression(expression, NO_ARGUMENTS);
                        continue;
                    }

                    final ArrayList<Expression> arguments = new ArrayList<>();
                    for (; tokenizer1.getTokenType() != TokenType.END; tokenizer1.advance()) {
                        final Expression argument = parse(tokenizer1, symbolLookup, assemblyMessageConsumer);

                        // If we couldn't parse a valid argument, give up parsing the argument list.
                        if (argument == null) {
                            break;
                        }

                        arguments.add(argument);

                        final TokenType tokenType1 = tokenizer1.getTokenType();
                        if (tokenType1 == TokenType.CLOSING_PARENTHESIS) {
                            tokenizer1.advance();
                            expression = new FunctionCallExpression(expression, arguments);
                            continue outer;
                        }

                        if (tokenType1 != TokenType.COMMA) {
                            // Give up parsing the argument list.
                            break;
                        }
                    }

                    break outer;

                case OPENING_BRACKET:
                    tokenizer1 = tokenizer.duplicateAndAdvance();

                    // Parse the index expression between the brackets.
                    final Expression indexExpression = parse(tokenizer1, symbolLookup, assemblyMessageConsumer);
                    if (indexExpression == null) {
                        break outer;
                    }

                    if (tokenizer1.getTokenType() != TokenType.CLOSING_BRACKET) {
                        break outer;
                    }

                    tokenizer1.advance();
                    expression = new IndexerExpression(expression, indexExpression, symbolLookup);
                    break;

                case PERIOD:
                    tokenizer1 = tokenizer.duplicateAndAdvance();

                    final Expression rightOperand = parseLevel0(tokenizer1, symbolLookup, assemblyMessageConsumer);
                    if (rightOperand == null) {
                        break outer;
                    }

                    expression = new PeriodExpression(expression, rightOperand, symbolLookup);
                    break;

                default:
                    break outer;
                }
            }
        }

        return expression;
    }

    @CheckForNull
    private static Expression parseLevel2(@Nonnull Tokenizer tokenizer, @CheckForNull SymbolLookup symbolLookup,
            @CheckForNull Z80BinaryOperator referenceOperator, @CheckForNull Consumer<AssemblyMessage> assemblyMessageConsumer)
            throws InvalidTokenException {
        Expression expression = parseLevel1(tokenizer, symbolLookup, assemblyMessageConsumer);
        if (expression != null) {
            Tokenizer tokenizer1;
            for (;; tokenizer.copyFrom(tokenizer1)) {
                if (tokenizer.getTokenType() == TokenType.PLUS_OR_MINUS_SEQUENCE) {
                    tokenizer.breakSequence();
                }

                Z80BinaryOperator operator = parseBinaryOperator(tokenizer);
                if (operator == null) {
                    break;
                }

                tokenizer1 = tokenizer.duplicateAndAdvance();

                // Honor operator precedence.
                if (referenceOperator != null && operator.getPriority() >= referenceOperator.getPriority()) {
                    break;
                }

                final Expression rightOperand = parseLevel2(tokenizer1, symbolLookup, operator, assemblyMessageConsumer);
                if (rightOperand == null) {
                    break;
                }

                expression = new BinaryOperatorExpression(operator.getOperator(), expression, rightOperand);
            }
        }

        return expression;
    }

    @CheckForNull
    private static Expression parseLevel3(@Nonnull Tokenizer tokenizer, @CheckForNull SymbolLookup symbolLookup,
            @CheckForNull Consumer<AssemblyMessage> assemblyMessageConsumer) throws InvalidTokenException {
        // Anonymous symbols are only accepted when they stand alone in the expression, or if they appear alone within parentheses,
        // within brackets, in either part of a conditional expression or in an argument.

        final CharSequence tokenText = tokenizer.getTokenText();
        if (tokenizer.getTokenType() == TokenType.PLUS_OR_MINUS_SEQUENCE) {
            Tokenizer tokenizer1 = tokenizer.duplicateAndAdvance();
            final TokenType tokenType = tokenizer1.getTokenType();
            switch (tokenType) {
            case END:
            case CLOSING_PARENTHESIS:
            case CLOSING_BRACKET:
            case CONDITIONAL_OPERATOR_FIRST:
            case CONDITIONAL_OPERATOR_SECOND:
            case COMMA:
                tokenizer.copyFrom(tokenizer1);
                return new IdentifierExpression(tokenText.toString(), symbolLookup);

            default:
                break;
            }
        }

        return parseLevel2(tokenizer, symbolLookup, null, assemblyMessageConsumer);
    }

    private static long parseUnsignedLongWithOverflow(@Nonnull CharSequence value, int radix,
            @CheckForNull Consumer<AssemblyMessage> assemblyMessageConsumer) {
        long result = 0;
        boolean overflow = false;

        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            int digit;

            // Compute the value of the current digit.
            if (Syntax.isDigit(ch)) {
                digit = ch - '0';
            } else {
                assert Syntax.isHexDigit(ch);

                // Force the letter to upper case.
                digit = (ch & 0x5F) - 'A' + 10;
            }

            assert digit < radix;

            // Update the result with this digit while detecting overflow.
            // http://stackoverflow.com/questions/8534107/detecting-multiplication-of-uint64-t-integers-overflow-with-c
            long a = radix, b = result;

            // The 32-bit overflow check on a is omitted, because radix is at most 16 here.

            long c = b >>> 32; // upper 32 bits of b
            long d = b & 0xFFFFFFFFL; // lower 32 bits of b

            long r = a * c;
            long s = a * d;
            if (r > 0xFFFFFFFFL) {
                overflow = true;
            }

            r <<= 32;

            if (UnsignedLongs.compare(s + r, s) < 0) {
                overflow = true;
            }

            result = s + r;

            // If result + digit < result, the addition overflowed.
            if (UnsignedLongs.compare(result + digit, result) < 0) {
                overflow = true;
            }

            result += digit;
        }

        if (overflow) {
            if (assemblyMessageConsumer != null) {
                assemblyMessageConsumer.accept(new OverflowInLiteralWarningMessage(value.toString()));
            }
        }

        return result;
    }

    // This class is not meant to be instantiated.
    private ExpressionParser() {
    }

}
