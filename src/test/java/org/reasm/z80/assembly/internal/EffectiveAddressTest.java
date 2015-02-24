package org.reasm.z80.assembly.internal;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.reasm.AssemblyMessage;
import org.reasm.SignedIntValue;
import org.reasm.StaticSymbol;
import org.reasm.UnsignedIntValue;
import org.reasm.Value;
import org.reasm.commons.messages.InvalidTokenErrorMessage;
import org.reasm.commons.messages.SyntaxErrorInEffectiveAddressErrorMessage;
import org.reasm.commons.messages.ValueOutOfRangeErrorMessage;
import org.reasm.expressions.EvaluationContext;
import org.reasm.expressions.SymbolLookup;
import org.reasm.testhelpers.AssemblyMessageCollector;
import org.reasm.testhelpers.EquivalentAssemblyMessage;
import org.reasm.testhelpers.SingleSymbolLookup;
import org.reasm.z80.expressions.internal.Tokenizer;

import ca.fragag.Consumer;

/**
 * Test class for {@link EffectiveAddress}.
 *
 * @author Francis Gagné
 */
@RunWith(Parameterized.class)
public class EffectiveAddressTest {

    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private static final ArrayList<Object[]> TEST_DATA = new ArrayList<>();

    static {
        addDataItem("", null, new SyntaxErrorInEffectiveAddressErrorMessage());
        addDataItem("!", null, new SyntaxErrorInEffectiveAddressErrorMessage());
        addDataItem("0A", null, new InvalidTokenErrorMessage("0A"));
        addDataItem("B", AddressingMode.REGISTER_B);
        addDataItem("C", AddressingMode.REGISTER_C);
        addDataItem("D", AddressingMode.REGISTER_D);
        addDataItem("E", AddressingMode.REGISTER_E);
        addDataItem("H", AddressingMode.REGISTER_H);
        addDataItem("L", AddressingMode.REGISTER_L);
        addDataItem("(HL)", AddressingMode.REGISTER_HL_INDIRECT);
        addDataItem("A", AddressingMode.REGISTER_A);
        addDataItem("BC", AddressingMode.REGISTER_BC);
        addDataItem("DE", AddressingMode.REGISTER_DE);
        addDataItem("HL", AddressingMode.REGISTER_HL);
        addDataItem("SP", AddressingMode.REGISTER_SP);
        addDataItem("(BC)", AddressingMode.REGISTER_BC_INDIRECT);
        addDataItem("(DE)", AddressingMode.REGISTER_DE_INDIRECT);
        addDataItem("IX", AddressingMode.REGISTER_IX);
        addDataItem("IY", AddressingMode.REGISTER_IY);
        addDataItem("(IX)", AddressingMode.REGISTER_IX_INDIRECT);
        addDataItem("(IY)", AddressingMode.REGISTER_IY_INDIRECT);
        addDataItem("(IX+0)", AddressingMode.REGISTER_IX_INDEXED);
        addDataItem("(IX+7Fh)", AddressingMode.REGISTER_IX_INDEXED, (byte) 0x7F);
        addDataItem("(IX+80h)", AddressingMode.REGISTER_IX_INDEXED, (byte) 0x80, new ValueOutOfRangeErrorMessage(0x80));
        addDataItem("(IX+0FF7Fh)", AddressingMode.REGISTER_IX_INDEXED, (byte) 0x7F, new ValueOutOfRangeErrorMessage(-0x81));
        addDataItem("(IX+0FF80h)", AddressingMode.REGISTER_IX_INDEXED, (byte) 0x80);
        addDataItem("(IX+0FFFFh)", AddressingMode.REGISTER_IX_INDEXED, (byte) 0xFF);
        addDataItem("(IX+10000h)", AddressingMode.REGISTER_IX_INDEXED, (byte) 0x00);
        addDataItem("(IX+'A')", AddressingMode.REGISTER_IX_INDEXED, (byte) 0x41);
        addDataItem("(IX+FOO)", AddressingMode.REGISTER_IX_INDEXED, (byte) 0x00);
        addDataItem("(IX+FOO)", new SingleSymbolLookup("FOO", new StaticSymbol(new UnsignedIntValue(42))),
                AddressingMode.REGISTER_IX_INDEXED, (byte) 0x2A);
        addDataItem("(IX-1)", AddressingMode.REGISTER_IX_INDEXED, (byte) 0xFF);
        addDataItem("(IX-80h)", AddressingMode.REGISTER_IX_INDEXED, (byte) 0x80);
        addDataItem("(IX-81h)", AddressingMode.REGISTER_IX_INDEXED, (byte) 0x7F, new ValueOutOfRangeErrorMessage(-0x81));
        addDataItem("(IX-'A')", AddressingMode.REGISTER_IX_INDEXED, (byte) -0x41);
        addDataItem("(IY+0)", AddressingMode.REGISTER_IY_INDEXED);
        addDataItem("AF", AddressingMode.REGISTER_AF);
        addDataItem("AF'", AddressingMode.REGISTER_AF_ALTERNATE);
        addDataItem("I", AddressingMode.REGISTER_I);
        addDataItem("R", AddressingMode.REGISTER_R);
        addDataItem("(C)", AddressingMode.REGISTER_C_INDIRECT);
        addDataItem("0", AddressingMode.IMMEDIATE, new UnsignedIntValue(0));
        addDataItem("123", AddressingMode.IMMEDIATE, new UnsignedIntValue(123));
        addDataItem("1234h", AddressingMode.IMMEDIATE, new UnsignedIntValue(0x1234));
        addDataItem("0FFFFFFFFFFFFFFFFh", AddressingMode.IMMEDIATE, new UnsignedIntValue(-1));
        addDataItem("-1", AddressingMode.IMMEDIATE, new SignedIntValue(-1));
        addDataItem("-0FFFFFFFFFFFFFFFFh", AddressingMode.IMMEDIATE, new SignedIntValue(1));
        addDataItem("(0)", AddressingMode.IMMEDIATE_INDIRECT, new UnsignedIntValue(0));
        addDataItem("(123)", AddressingMode.IMMEDIATE_INDIRECT, new UnsignedIntValue(123));
        addDataItem("(1234h)", AddressingMode.IMMEDIATE_INDIRECT, new UnsignedIntValue(0x1234));
        addDataItem("F", AddressingMode.IMMEDIATE);
        addDataItem("(F)", AddressingMode.IMMEDIATE_INDIRECT);
        addDataItem("(IX*2)", AddressingMode.IMMEDIATE_INDIRECT);
        addDataItem("(2+2)", AddressingMode.IMMEDIATE_INDIRECT, new UnsignedIntValue(4));
        addDataItem("(IZ+2)", AddressingMode.IMMEDIATE_INDIRECT);
    }

    /**
     * Gets the test data for this parameterized test.
     *
     * @return the test data
     */
    @Nonnull
    @Parameters
    public static List<Object[]> data() {
        return TEST_DATA;
    }

    private static void addDataItem(@Nonnull String text, @Nonnull AddressingMode expectedAddressingMode) {
        addDataItem(text, null, expectedAddressingMode, (byte) 0, null, null);
    }

    private static void addDataItem(@Nonnull String text, @CheckForNull AddressingMode expectedAddressingMode,
            @Nonnull AssemblyMessage expectedMessage) {
        addDataItem(text, null, expectedAddressingMode, (byte) 0, null, expectedMessage);
    }

    private static void addDataItem(@Nonnull String text, @Nonnull AddressingMode expectedAddressingMode, byte expectedDisplacement) {
        addDataItem(text, null, expectedAddressingMode, expectedDisplacement, null, null);
    }

    private static void addDataItem(@Nonnull String text, @Nonnull AddressingMode expectedAddressingMode,
            byte expectedDisplacement, @Nonnull AssemblyMessage expectedMessage) {
        addDataItem(text, null, expectedAddressingMode, expectedDisplacement, null, expectedMessage);
    }

    private static void addDataItem(@Nonnull String text, @Nonnull AddressingMode expectedAddressingMode,
            @Nonnull Value expectedImmediate) {
        addDataItem(text, null, expectedAddressingMode, (byte) 0, expectedImmediate, null);
    }

    private static void addDataItem(@Nonnull String text, @Nonnull SymbolLookup symbolLookup,
            @Nonnull AddressingMode expectedAddressingMode, byte expectedDisplacement) {
        addDataItem(text, symbolLookup, expectedAddressingMode, expectedDisplacement, null, null);
    }

    private static void addDataItem(@Nonnull String text, @CheckForNull SymbolLookup symbolLookup,
            @CheckForNull AddressingMode expectedAddressingMode, byte expectedDisplacement, @CheckForNull Value expectedImmediate,
            @CheckForNull AssemblyMessage expectedMessage) {
        TEST_DATA.add(new Object[] { text, symbolLookup, expectedAddressingMode, expectedDisplacement, expectedImmediate,
                expectedMessage });
    }

    @Nonnull
    private final String text;
    @CheckForNull
    private final SymbolLookup symbolLookup;
    @CheckForNull
    private final AddressingMode expectedAddressingMode;
    private final byte expectedDisplacement;
    @CheckForNull
    private final Value expectedImmediate;
    @CheckForNull
    private final AssemblyMessage expectedMessage;

    /**
     * Initializes a new EffectiveAddressTest.
     *
     * @param text
     *            the text of the effective address
     * @param symbolLookup
     *            the {@link SymbolLookup} to use
     * @param expectedAddressingMode
     *            the expected addressing mode
     * @param expectedDisplacement
     *            the expected displacement value
     * @param expectedImmediate
     *            the expected immediate value
     * @param expectedMessage
     *            the expected assembly message
     */
    public EffectiveAddressTest(@Nonnull String text, @CheckForNull SymbolLookup symbolLookup,
            @CheckForNull AddressingMode expectedAddressingMode, byte expectedDisplacement, @CheckForNull Value expectedImmediate,
            @CheckForNull AssemblyMessage expectedMessage) {
        this.text = text;
        this.symbolLookup = symbolLookup;
        this.expectedAddressingMode = expectedAddressingMode;
        this.expectedDisplacement = expectedDisplacement;
        this.expectedImmediate = expectedImmediate;
        this.expectedMessage = expectedMessage;
    }

    /**
     * Asserts that
     * {@link EffectiveAddress#getEffectiveAddress(Tokenizer, SymbolLookup, EvaluationContext, Charset, Consumer, EffectiveAddress)}
     * correctly identifies an effective address.
     */
    @Test
    public void getEffectiveAddress() {
        final EffectiveAddress ea = new EffectiveAddress();
        final Tokenizer tokenizer = new Tokenizer();
        tokenizer.setCharSequence(this.text);
        final ArrayList<AssemblyMessage> messages = new ArrayList<>();
        final AssemblyMessageCollector messageCollector = new AssemblyMessageCollector(messages);
        EffectiveAddress.getEffectiveAddress(tokenizer, this.symbolLookup, new EvaluationContext(null, 0, messageCollector), UTF_8,
                messageCollector, ea);
        assertThat(ea.addressingMode, is(this.expectedAddressingMode));
        assertThat(ea.displacement, is(this.expectedDisplacement));
        assertThat(ea.immediate, is(this.expectedImmediate));
        if (this.expectedMessage == null) {
            assertThat(messages, is(empty()));
        } else {
            assertThat(messages, contains(new EquivalentAssemblyMessage(this.expectedMessage)));
        }
    }
}
