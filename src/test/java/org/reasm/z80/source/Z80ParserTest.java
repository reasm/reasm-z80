package org.reasm.z80.source;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.reasm.commons.source.Syntax;

/**
 * Test class for {@link Z80Parser}.
 *
 * @author Francis Gagné
 */
public class Z80ParserTest {

    /**
     * Asserts that {@link Syntax#isValidIdentifierCodePoint(int)} returns <code>true</code> for code points that are valid as part
     * of an identifier and <code>false</code> for other code points.
     */
    @Test
    public void isValidIdentifierCodePoint() {
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint(0), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('\t'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('\n'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint(0xB), is(true)); // LINE TABULATION
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('\f'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('\r'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint(' '), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('!'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('"'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('#'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('$'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('\''), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('.'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('0'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('@'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('A'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('\\'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('`'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint('a'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierCodePoint(-1), is(false));
    }

    /**
     * Asserts that {@link Syntax#isValidIdentifierInitialCodePoint(int)} returns <code>true</code> for code points that are valid
     * as the first code point of an identifier and <code>false</code> for other code points.
     */
    @Test
    public void isValidIdentifierInitialCodePoint() {
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint(0), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('\t'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('\n'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint(0xB), is(true)); // LINE TABULATION
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('\f'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('\r'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint(' '), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('!'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('"'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('#'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('$'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('\''), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('.'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('0'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('@'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('A'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('\\'), is(false));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('`'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint('a'), is(true));
        assertThat(Z80Parser.SYNTAX.isValidIdentifierInitialCodePoint(-1), is(false));
    }

}
