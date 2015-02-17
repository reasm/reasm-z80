package org.reasm.z80.source;

import javax.annotation.Nonnull;

import org.reasm.commons.source.BlockDirective;
import org.reasm.commons.source.BlockParser;
import org.reasm.commons.source.Parser;
import org.reasm.commons.source.Syntax;

import com.google.common.collect.ImmutableMap;

/**
 * The parser for Z80 family assembler source files.
 *
 * @author Francis Gagné
 */
public final class Z80Parser {

    /** The syntax rules for the M68000 family assembly language. */
    @Nonnull
    public static final Syntax SYNTAX;

    static {
        final int[] invalidIdentifierCodePoints = new int[] {

                '!', // logical NOT operator; "strictly different from" operator
             //'"', // string delimiter; allowed in identifiers, except as the first character
             //'#', // not assigned
             //'$', // not assigned
                '%', // modulo operator; prefix for binary integer literals
                '&', // bitwise AND operator; logical AND operator
                //'\'', // string delimiter; allowed in identifiers, except as the first character
                '(', // grouping left parenthesis; start of function call argument list
                ')', // grouping right parenthesis; end of function call argument list
                '*', // multiplication operator; part of index register scale specification
                '+', // addition operator; unary plus operator
                ',', // operand separator, argument separator
                '-', // subtraction operator; negation operator; part of register range specification
                //'.', // pseudo object member accessor (actually part of the symbol name)
                '/', // division operator; part of register list specification
                ':', // end of label; conditional operator, second part
                ';', // start of comment
                '<', // "less than" operator; "less than or equal to" operator; "different from" operator; bit shift left operator
                '=', // "equal to" operator; "strictly equal to" operator; "strictly different from" operator; "less than or equal to" operator; "greater than or equal to" operator
                '>', // "greater than" operator; "greater than or equal to" operator; "different from" operator; bit shift right operator
                '?', // conditional operator, first part
                //'@', // prefix for local symbols (part of the symbol name)
                '[', // start of array indexer
                '\\', // prefix for reference to a macro argument
                ']', // end of array indexer
                '^', // bitwise XOR operator
                //'`', // not assigned
                //'{', // not assigned
                '|', // bitwise OR operator; logical OR operator
                //'}', // not assigned
                '~', // bitwise NOT operator

        };

        final int[] invalidIdentifierInitialCodePoints = new int[] { '"', '\'' };

        SYNTAX = new Syntax(invalidIdentifierCodePoints, invalidIdentifierInitialCodePoints);
    }

    @Nonnull
    private static final ImmutableMap<BlockDirective, BlockParser> BLOCKS;

    static {
        final ImmutableMap.Builder<BlockDirective, BlockParser> blocks = ImmutableMap.builder();
        blocks.put(Z80BlockDirectives.DO, BlockParsers.DO);
        blocks.put(Z80BlockDirectives.FOR, BlockParsers.FOR);
        blocks.put(Z80BlockDirectives.IF, BlockParsers.IF);
        blocks.put(Z80BlockDirectives.MACRO, BlockParsers.MACRO);
        blocks.put(Z80BlockDirectives.NAMESPACE, BlockParsers.NAMESPACE);
        blocks.put(Z80BlockDirectives.PHASE, BlockParsers.PHASE);
        blocks.put(Z80BlockDirectives.REPT, BlockParsers.REPT);
        blocks.put(Z80BlockDirectives.TRANSFORM, BlockParsers.TRANSFORM);
        blocks.put(Z80BlockDirectives.WHILE, BlockParsers.WHILE);
        BLOCKS = blocks.build();
    }

    /** The Z80 family parser. */
    @Nonnull
    public static final Parser INSTANCE = new Parser(SYNTAX, Z80BlockDirectives.MAP, BLOCKS, Z80LogicalLineFactory.INSTANCE,
            Z80BlockDirectiveLineFactory.INSTANCE);

    // This class is not meant to be instantiated.
    private Z80Parser() {
    }

}
