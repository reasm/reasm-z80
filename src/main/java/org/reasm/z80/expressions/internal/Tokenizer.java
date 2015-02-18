package org.reasm.z80.expressions.internal;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.reasm.commons.source.Syntax;
import org.reasm.commons.util.CharSequenceParserReader;
import org.reasm.z80.source.Z80Parser;

/**
 * The tokenizer for expressions.
 *
 * @author Francis Gagné
 */
public final class Tokenizer {

    private static final int INTEGER_TYPE_BINARY = 0;
    private static final int INTEGER_TYPE_DECIMAL = 1;
    private static final int INTEGER_TYPE_HEXADECIMAL = 2;

    private CharSequenceParserReader reader;
    private int endOfBrokenSequence;
    private TokenType tokenType;
    private int tokenStart;
    private int tokenEnd;

    /**
     * Initializes a new Tokenizer.
     */
    public Tokenizer() {
    }

    /**
     * Initializes a new Tokenizer from another Tokenizer.
     *
     * @param tokenizer
     *            the other tokenizer to copy
     */
    private Tokenizer(@Nonnull Tokenizer tokenizer) {
        this.reader = tokenizer.reader.duplicate();
        this.endOfBrokenSequence = tokenizer.endOfBrokenSequence;
        this.tokenType = tokenizer.tokenType;
        this.tokenStart = tokenizer.tokenStart;
        this.tokenEnd = tokenizer.tokenEnd;
    }

    /**
     * Advances to the next token.
     *
     * @see #getTokenType()
     * @see #getTokenStart()
     * @see #getTokenEnd()
     * @see #getTokenLength()
     * @see #getTokenText()
     */
    public final void advance() {
        if (this.endOfBrokenSequence != -1) {
            int start = this.tokenEnd;
            if (start < this.endOfBrokenSequence) {
                this.setToken(TokenType.OPERATOR, start, start + 1);
                return;
            }

            this.endOfBrokenSequence = -1;
        }

        this.setToken(TokenType.END, this.tokenEnd, this.tokenEnd);

        while (Syntax.isWhitespace(this.reader.getCurrentCodePoint())) {
            this.reader.advance();
        }

        final int start = this.reader.getPosition();
        TokenType tokenType;

        final int firstCodePoint = this.reader.getCurrentCodePoint();
        int codePoint;
        switch (firstCodePoint) {
        case -1:
            return;

        case '!': // either "!" or "!="
        case '=': // either "=" or "=="
            tokenType = TokenType.OPERATOR;
            this.reader.advance();

            switch (this.reader.getCurrentCodePoint()) {
            case '=':
                this.reader.advance();
                break;
            }

            break;

        case '"': // a string delimited by double quotes
        case '\'': // a string delimited by apostrophes
            tokenType = TokenType.STRING;
            this.reader.advance();

            boolean lastWasEscape = false;
            for (;; this.reader.advance()) {
                codePoint = this.reader.getCurrentCodePoint();
                if (codePoint == -1) {
                    // The string is not terminated properly: make the token invalid.
                    tokenType = TokenType.INVALID;
                    break;
                }

                if (lastWasEscape) {
                    lastWasEscape = false;
                } else {
                    if (codePoint == firstCodePoint) {
                        // Finish the string.
                        this.reader.advance();
                        break;
                    }

                    lastWasEscape = codePoint == '\\';
                }
            }

            break;

        case '%':
        case '*':
        case '/':
        case '^':
        case '~':
            tokenType = TokenType.OPERATOR;
            this.reader.advance();
            break;

        case '&': // either '&' or '&&'
            tokenType = TokenType.OPERATOR;
            this.reader.advance();

            switch (this.reader.getCurrentCodePoint()) {
            case '&':
                this.reader.advance();
                break;
            }

            break;

        case '(':
            tokenType = TokenType.OPENING_PARENTHESIS;
            this.reader.advance();
            break;

        case ')':
            tokenType = TokenType.CLOSING_PARENTHESIS;
            this.reader.advance();
            break;

        case '+': // one or more '+'
        case '-': // one or more '-'
            tokenType = TokenType.PLUS_OR_MINUS_SEQUENCE;
            this.reader.advance();

            while (this.reader.getCurrentCodePoint() == firstCodePoint) {
                this.reader.advance();
            }

            break;

        case ',':
            tokenType = TokenType.COMMA;
            this.reader.advance();
            break;

        case ':':
            tokenType = TokenType.CONDITIONAL_OPERATOR_SECOND;
            this.reader.advance();
            break;

        case ';': // a comment (not supposed to happen!)
            tokenType = TokenType.INVALID;
            this.reader.advance();
            break;

        case '<': // either "<", "<<", "<=", or "<>"
            tokenType = TokenType.OPERATOR;
            this.reader.advance();

            switch (this.reader.getCurrentCodePoint()) {
            case '<':
            case '=':
            case '>':
                this.reader.advance();
                break;
            }

            break;

        case '>': // either ">", ">=" or ">>"
            tokenType = TokenType.OPERATOR;
            this.reader.advance();

            switch (this.reader.getCurrentCodePoint()) {
            case '=':
            case '>':
                this.reader.advance();
                break;
            }

            break;

        case '?':
            tokenType = TokenType.CONDITIONAL_OPERATOR_FIRST;
            this.reader.advance();
            break;

        case '[':
            tokenType = TokenType.OPENING_BRACKET;
            this.reader.advance();
            break;

        case '\\':
            tokenType = TokenType.INVALID;
            this.reader.advance();
            for (; Z80Parser.SYNTAX.isValidIdentifierCodePoint(codePoint = this.reader.getCurrentCodePoint()); this.reader
                    .advance()) {
            }

            break;

        case ']':
            tokenType = TokenType.CLOSING_BRACKET;
            this.reader.advance();
            break;

        case '|':
            tokenType = TokenType.OPERATOR;
            this.reader.advance();

            switch (this.reader.getCurrentCodePoint()) {
            case '|':
                this.reader.advance();
                break;
            }

            break;

        default:
            if (firstCodePoint == '.' || Syntax.isDigit(firstCodePoint)) {
                // If it's a digit, then it's an integer or a real. Assume it's a decimal integer literal for now.
                // If it's a point, then it's an operator or a real. In the first pass in the loop below, the point will be found
                // and the token type will switch to REAL if there is a valid real.
                tokenType = TokenType.DECIMAL_INTEGER;

                // Keep track of if the characters we've seen so far
                // are valid for a binary integer, a decimal integer and an hexadecimal integer.
                // INTEGER_TYPE_BINARY means valid for all three.
                // INTEGER_TYPE_DECIMAL means valid for decimal or hexadecimal.
                // INTEGER_TYPE_HEXADECIMAL means valid for hexadecimal only.
                int integerType = INTEGER_TYPE_BINARY;

                // The 'B' and 'H' suffixes must be handled specially,
                // because we're not doing any look-ahead.
                boolean haveBinarySuffix = false;
                boolean haveHexadecimalSuffix = false;

                codePoint = firstCodePoint;
                for (; codePoint != -1; this.reader.advance(), codePoint = this.reader.getCurrentCodePoint()) {
                    // If the next character is not a valid identifier character, it's the end of the integer token.
                    if (!Z80Parser.SYNTAX.isValidIdentifierCodePoint(codePoint)) {
                        break;
                    }

                    if (haveHexadecimalSuffix) {
                        // 'H' and 'h' aren't valid digits.
                        tokenType = TokenType.INVALID;
                        this.finishIdentifier();
                        break;
                    }

                    if (haveBinarySuffix) {
                        // Switch to hexadecimal, because 'B' and 'h' are valid hexadecimal digits.
                        integerType = INTEGER_TYPE_HEXADECIMAL;
                        haveBinarySuffix = false;
                    }

                    // If the character is a point, try to parse a real number.
                    if (codePoint == '.') {
                        if (integerType == INTEGER_TYPE_HEXADECIMAL) {
                            // There are hexadecimal digits before the point:
                            // the token is invalid.
                            tokenType = TokenType.INVALID;
                            this.finishIdentifier();
                            break;
                        }

                        CharSequenceParserReader reader2 = this.reader.duplicate();
                        reader2.advance();
                        codePoint = reader2.getCurrentCodePoint();

                        // If the decimal point is followed by a character that is not a valid identifier character, keep the point
                        // as part of this token, unless the token is only a point.
                        if (!Z80Parser.SYNTAX.isValidIdentifierCodePoint(codePoint)) {
                            if (firstCodePoint == '.') {
                                break;
                            }

                            this.reader.copyFrom(reader2);
                            tokenType = TokenType.REAL;
                            break;
                        }

                        // If the decimal point is followed by a valid identifier character that is not a digit, then reject the
                        // point as a decimal separator and stay with the integer. The point will then be parsed as an operator
                        // and an identifier will follow it.
                        if (!Syntax.isDigit(codePoint)) {
                            break;
                        }

                        this.reader.copyFrom(reader2);

                        tokenType = this.readRealDigits(true);
                        if (tokenType == TokenType.INVALID) {
                            break;
                        }

                        codePoint = this.reader.getCurrentCodePoint();

                        // If the character is an 'E' or 'e', try to parse the exponential part of a floating-point number.
                        if (codePoint == 'E' || codePoint == 'e') {
                            this.reader.advance();
                            int codePoint2 = this.reader.getCurrentCodePoint();

                            // If the 'E' or 'e' is immediately followed by a '+' or '-', accept that character and advance
                            // the reader.
                            if (codePoint2 == '+' || codePoint2 == '-') {
                                this.reader.advance();
                                codePoint2 = this.reader.getCurrentCodePoint();

                                if (!Syntax.isDigit(codePoint2)) {
                                    // If the '+' or '-' is not followed by a digit, make the token invalid.
                                    tokenType = TokenType.INVALID;
                                    this.finishIdentifier();
                                    break;
                                }
                            } else if (!Syntax.isDigit(codePoint2)) {
                                // If the 'E' or 'e' is not followed by a '+', a '-' or a digit, make the token invalid.
                                tokenType = TokenType.INVALID;
                                this.finishIdentifier();
                                break;
                            }

                            tokenType = this.readRealDigits(false);
                            if (tokenType == TokenType.INVALID) {
                                break;
                            }

                            codePoint = this.reader.getCurrentCodePoint();
                        }
                    }

                    // If the token is now a real, we've reached the end of it already.
                    if (tokenType == TokenType.REAL) {
                        break;
                    }

                    if (codePoint == 'B' || codePoint == 'b') {
                        // Don't switch to hexadecimal yet.
                        // If this 'B' or 'b' turns out to be the last character in the literal,
                        // integerType must keep its actual value.
                        // If it's not, we'll switch integerType to INTEGER_TYPE_HEXADECIMAL then.
                        haveBinarySuffix = true;
                    } else if (codePoint == 'H' || codePoint == 'h') {
                        haveHexadecimalSuffix = true;
                    } else if (Syntax.isBinDigit(codePoint)) {
                        // Nothing to do.
                    } else if (Syntax.isDigit(codePoint)) {
                        if (integerType < INTEGER_TYPE_DECIMAL) {
                            integerType = INTEGER_TYPE_DECIMAL;
                        }
                    } else if (Syntax.isHexDigit(codePoint)) {
                        integerType = INTEGER_TYPE_HEXADECIMAL;
                    } else {
                        tokenType = TokenType.INVALID;
                        this.finishIdentifier();
                        break;
                    }
                }

                // If the first character was a point and the reader is still at its initial position, parse the period operator.
                if (firstCodePoint == '.' && this.reader.getPosition() == start) {
                    tokenType = TokenType.PERIOD;
                    this.reader.advance();
                } else {
                    // Handle the 'B' and 'H' suffixes.
                    if (haveBinarySuffix) {
                        // 'B' suffix: if there are digits other than '0' and '1', the token is invalid.
                        if (integerType == INTEGER_TYPE_BINARY) {
                            tokenType = TokenType.BINARY_INTEGER;
                        } else {
                            tokenType = TokenType.INVALID;
                        }
                    } else if (haveHexadecimalSuffix) {
                        tokenType = TokenType.HEXADECIMAL_INTEGER;
                    } else if (integerType == INTEGER_TYPE_HEXADECIMAL) {
                        // No suffix: if there are hexadecimal digits, the token is invalid.
                        tokenType = TokenType.INVALID;
                    }
                }
            } else {
                assert Z80Parser.SYNTAX.isValidIdentifierCodePoint(firstCodePoint);

                // If it's a valid code point for an identifier, then it's an identifier.
                tokenType = TokenType.IDENTIFIER;
                this.finishIdentifier();
            }

            break;
        }

        this.setToken(tokenType, start, this.reader.getPosition());
    }

    /**
     * Breaks a token of type {@link TokenType#PLUS_OR_MINUS_SEQUENCE} into a series of {@link TokenType#OPERATOR} tokens.
     *
     * @throws IllegalStateException
     *             the current token is not of type {@link TokenType#PLUS_OR_MINUS_SEQUENCE}
     * @see #getTokenType()
     * @see #getTokenStart()
     * @see #getTokenEnd()
     * @see #getTokenLength()
     * @see #getTokenText()
     */
    public final void breakSequence() {
        if (this.tokenType != TokenType.PLUS_OR_MINUS_SEQUENCE) {
            throw new IllegalStateException("The current token's type is not PLUS_OR_MINUS_SEQUENCE");
        }

        this.endOfBrokenSequence = this.tokenEnd;
        this.setToken(TokenType.OPERATOR, this.tokenStart, this.tokenStart + 1);
    }

    /**
     * Copies the state from another Tokenizer that reads from the same {@link CharSequence}, usually a Tokenizer returned by
     * {@link #duplicateAndAdvance()}.
     *
     * @param other
     *            the other Tokenizer
     * @see #duplicateAndAdvance()
     */
    public final void copyFrom(@Nonnull Tokenizer other) {
        this.reader.copyFrom(other.reader);
        this.endOfBrokenSequence = other.endOfBrokenSequence;
        this.tokenType = other.tokenType;
        this.tokenStart = other.tokenStart;
        this.tokenEnd = other.tokenEnd;
    }

    /**
     * Creates a copy of this tokenizer and advances it to the next token.
     *
     * @return the new Tokenizer
     * @see #copyFrom(Tokenizer)
     */
    @Nonnull
    public final Tokenizer duplicateAndAdvance() {
        final Tokenizer duplicate = new Tokenizer(this);
        duplicate.advance();
        return duplicate;
    }

    /**
     * Gets the ending position of this tokenizer's current token.
     *
     * @return the current token's ending position
     */
    public final int getTokenEnd() {
        return this.tokenEnd;
    }

    /**
     * Gets the length of this tokenizer's current token.
     *
     * @return the current token's length
     */
    public final int getTokenLength() {
        return this.tokenEnd - this.tokenStart;
    }

    /**
     * Gets the starting position of this tokenizer's current token.
     *
     * @return the current token's starting position
     */
    public final int getTokenStart() {
        return this.tokenStart;
    }

    /**
     * Gets the text of this tokenizer's current token.
     *
     * @return the current token's text
     */
    @Nonnull
    public final CharSequence getTokenText() {
        return this.reader.getCharSequence().subSequence(this.tokenStart, this.tokenEnd);
    }

    /**
     * Gets the type of this tokenizer's current token.
     *
     * @return the current token's type
     */
    public final TokenType getTokenType() {
        return this.tokenType;
    }

    /**
     * Sets that {@link CharSequence} this tokenizer will read from. The first token is parsed.
     *
     * @param charSequence
     *            the {@link CharSequence} to read from
     */
    public final void setCharSequence(@Nonnull CharSequence charSequence) {
        if (charSequence == null) {
            throw new NullPointerException("charSequence");
        }

        this.reader = new CharSequenceParserReader(charSequence);
        this.endOfBrokenSequence = -1;
        this.setToken(TokenType.END, 0, 0);
        this.advance();
    }

    /**
     * Gets the character at the specified index in the text of this tokenizer's current token.
     *
     * @param index
     *            the index of the character to get
     * @return the character
     */
    public char tokenCharAt(int index) {
        return this.reader.getCharSequence().charAt(this.tokenStart + index);
    }

    /**
     * Determines whether the text of this tokenizer's current token is the same as the specified string.
     *
     * @param string
     *            the string to compare the token's text with
     * @return <code>true</code> if the token's text is equal to the string, otherwise <code>false</code>
     */
    public final boolean tokenEqualsString(@CheckForNull String string) {
        if (string == null) {
            return false;
        }

        if (this.getTokenLength() != string.length()) {
            return false;
        }

        for (int i = 0; i < this.getTokenLength(); i++) {
            if (this.tokenCharAt(i) != string.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Advances the reader until a code point that is not valid for an identifier is found.
     */
    private final void finishIdentifier() {
        int codePoint;
        do {
            this.reader.advance();
            codePoint = this.reader.getCurrentCodePoint();
        } while (Z80Parser.SYNTAX.isValidIdentifierCodePoint(codePoint));
    }

    @Nonnull
    private final TokenType readRealDigits(boolean acceptScientificENotation) {
        for (;;) {
            this.reader.advance();
            int codePoint = this.reader.getCurrentCodePoint();

            // If the next character is not a valid identifier character, it's the end of the real token.
            if (!Z80Parser.SYNTAX.isValidIdentifierCodePoint(codePoint)) {
                break;
            }

            // If scientific E notation is allowed at this point, and the next character is 'E' or 'e', stop here.
            if (acceptScientificENotation && (codePoint == 'E' || codePoint == 'e')) {
                break;
            }

            // If the next character is not a digit, make the token invalid.
            if (!Syntax.isDigit(codePoint)) {
                this.finishIdentifier();
                return TokenType.INVALID;
            }
        }

        return TokenType.REAL;
    }

    private final void setToken(@Nonnull TokenType tokenType, int tokenStart, int tokenEnd) {
        this.tokenType = tokenType;
        this.tokenStart = tokenStart;
        this.tokenEnd = tokenEnd;
    }

}
