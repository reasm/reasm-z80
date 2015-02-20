package org.reasm.z80.assembly.internal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.reasm.AssemblyMessage;
import org.reasm.SymbolContext;
import org.reasm.SymbolType;
import org.reasm.UnsignedIntValue;
import org.reasm.Value;
import org.reasm.testhelpers.UserSymbolMatcher;

/**
 * Test class for short M68000 programs consisting of directives that define symbols.
 *
 * @author Francis Gagné
 */
@RunWith(Parameterized.class)
public class SymbolsTest extends BaseProgramsTest {

    @Nonnull
    private static final UnsignedIntValue UINT_0 = new UnsignedIntValue(0);
    @Nonnull
    private static final UnsignedIntValue UINT_1 = new UnsignedIntValue(1);
    @Nonnull
    private static final UnsignedIntValue UINT_2 = new UnsignedIntValue(2);

    @Nonnull
    private static final UserSymbolMatcher<Value> FOO_CONSTANT_UINT_0 = new UserSymbolMatcher<>(SymbolContext.VALUE, "foo",
            SymbolType.CONSTANT, UINT_0);

    @Nonnull
    private static final ArrayList<Object[]> TEST_DATA = new ArrayList<>();

    static {
        // No mnemonic
        addDataItem("foo", 2, new UserSymbolMatcher[] { new UserSymbolMatcher<>(SymbolContext.VALUE, "foo", SymbolType.CONSTANT,
                UINT_0) });
        addDataItem("foo:", 2, new UserSymbolMatcher[] { new UserSymbolMatcher<>(SymbolContext.VALUE, "foo", SymbolType.CONSTANT,
                UINT_0) });
        // TODO: Reactivate this test when ORG is implemented
        //addDataItem(" ORG $456\nfoo", 3, new UserSymbolMatcher[] { new UserSymbolMatcher<>(SymbolContext.VALUE, "foo",
        //        SymbolType.CONSTANT, new UnsignedIntValue(0x456)) });
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

    private static void addDataItem(@Nonnull String code, int steps, @Nonnull UserSymbolMatcher<?>[] symbols) {
        addDataItem(code, steps, symbols, null);
    }

    private static void addDataItem(@Nonnull String code, int steps, @Nonnull UserSymbolMatcher<?>[] symbols,
            @CheckForNull AssemblyMessage expectedMessage) {
        TEST_DATA.add(new Object[] { code, steps, symbols, expectedMessage });
    }

    /**
     * Initializes a new SymbolsTest.
     *
     * @param code
     *            assembly code to assemble
     * @param steps
     *            the number of steps the program is expected to take to assemble completely
     * @param symbolMatchers
     *            an array of matchers for the symbols that are expected to be defined in the program
     * @param expectedMessage
     *            an {@link AssemblyMessage} that is expected to be generated while assembling the code
     */
    public SymbolsTest(@Nonnull String code, int steps, @Nonnull UserSymbolMatcher<?>[] symbolMatchers,
            @CheckForNull AssemblyMessage expectedMessage) {
        super(code, steps, NO_DATA, expectedMessage, null, symbolMatchers);
    }

}
