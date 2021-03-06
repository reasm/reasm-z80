package org.reasm.z80.assembly.internal;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import org.junit.Test;
import org.reasm.Architecture;
import org.reasm.Assembly;
import org.reasm.AssemblyCompletionStatus;
import org.reasm.AssemblyMessage;
import org.reasm.Configuration;
import org.reasm.Environment;
import org.reasm.FileFetcher;
import org.reasm.UserSymbol;
import org.reasm.commons.messages.AddressingModeNotAllowedHereErrorMessage;
import org.reasm.messages.UnresolvedSymbolReferenceErrorMessage;
import org.reasm.messages.WrongNumberOfOperandsErrorMessage;
import org.reasm.source.SourceFile;
import org.reasm.testhelpers.EquivalentAssemblyMessage;
import org.reasm.testhelpers.UserSymbolMatcher;
import org.reasm.z80.Z80Architecture;

/**
 * Base test class for short Z80 programs.
 *
 * @author Francis Gagné
 */
public abstract class BaseProgramsTest {

    @Nonnull
    static final byte[] NO_DATA = new byte[0];
    @Nonnull
    static final UserSymbolMatcher<?>[] NO_SYMBOLS = new UserSymbolMatcher[0];

    @Nonnull
    static final AssemblyMessage ADDRESSING_MODE_NOT_ALLOWED_HERE = new AddressingModeNotAllowedHereErrorMessage();
    @Nonnull
    static final AssemblyMessage WRONG_NUMBER_OF_OPERANDS = new WrongNumberOfOperandsErrorMessage();
    @Nonnull
    static final AssemblyMessage UNDEFINED_SYMBOL = new UnresolvedSymbolReferenceErrorMessage("UNDEFINED");

    @Nonnull
    private final String code;
    private final int steps;
    @Nonnull
    private final byte[] output;
    @CheckForNull
    private final AssemblyMessage expectedMessage;
    @CheckForNull
    private final AssemblyMessage[] expectedMessages;
    @CheckForNull
    private final UserSymbolMatcher<?>[] symbolMatchers;

    /**
     * Initializes a new BaseProgramsTest.
     *
     * @param code
     *            assembly code to assemble
     * @param steps
     *            the number of steps the program is expected to take to assemble completely
     * @param output
     *            the program's output
     * @param expectedMessage
     *            an {@link AssemblyMessage} that is expected to be generated while assembling the code
     * @param expectedMessages
     *            an array of {@link AssemblyMessage AssemblyMessages} that are expected to be generated while assembling the code.
     *            Takes priority over <code>expectedMessage</code>.
     * @param symbolMatchers
     *            an array of {@link UserSymbolMatcher UserSymbolMatchers} that match the {@link UserSymbol UserSymbols} that are
     *            expected to be generated while assembling the code, or <code>null</code> to omit checking the generated symbols
     */
    public BaseProgramsTest(@Nonnull String code, int steps, @Nonnull byte[] output, @CheckForNull AssemblyMessage expectedMessage,
            @CheckForNull AssemblyMessage[] expectedMessages, @CheckForNull UserSymbolMatcher<?>[] symbolMatchers) {
        this.code = code;
        this.steps = steps;
        this.output = output;
        this.expectedMessage = expectedMessage;
        this.expectedMessages = expectedMessages;
        this.symbolMatchers = symbolMatchers;
    }

    /**
     * Asserts that a program assembles correctly.
     *
     * @throws IOException
     *             an I/O exception occurred
     */
    @Test
    public void assemble() throws IOException {
        try {
            final Environment environment = this.getEnvironment();
            final SourceFile mainSourceFile = new SourceFile(this.code, null);
            final Configuration configuration = new Configuration(environment, mainSourceFile, Z80Architecture.INSTANCE)
                    .setFileFetcher(this.getFileFetcher());
            final Assembly assembly = new Assembly(configuration);

            int steps = 0;
            AssemblyCompletionStatus status;
            do {
                assertThat("The assembly is performing more steps than expected.", steps, is(lessThan(this.steps)));

                status = assembly.step();
                ++steps;
            } while (status != AssemblyCompletionStatus.COMPLETE);

            assertThat("The assembly is performing fewer steps than expected.", steps, is(this.steps));

            if (this.expectedMessages != null) {
                final EquivalentAssemblyMessage[] matchers = new EquivalentAssemblyMessage[this.expectedMessages.length];
                for (int i = 0; i < this.expectedMessages.length; i++) {
                    matchers[i] = new EquivalentAssemblyMessage(this.expectedMessages[i]);
                }

                assertThat(assembly.getMessages(), contains(matchers));
            } else if (this.expectedMessage != null) {
                assertThat(assembly.getMessages(), contains(new EquivalentAssemblyMessage(this.expectedMessage)));
            } else {
                assertThat(assembly.getMessages(), is(empty()));
            }

            if (this.symbolMatchers != null) {
                assertThat(assembly.getSymbols(), containsInAnyOrder(this.symbolMatchers));
            }

            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            assembly.writeAssembledDataTo(out);
            final byte[] outputBytes = out.toByteArray();
            assertThat(outputBytes.length, is(this.output.length));
            for (int i = 0; i < this.output.length; i++) {
                assertThat(outputBytes[i], is(this.output[i]));
            }
        } catch (AssertionError e) {
            throw new AssertionError(this.code + "\n" + e.getMessage(), e);
        }
    }

    /**
     * Gets the {@link Environment} to pass to {@link Configuration#Configuration(Environment, SourceFile, Architecture)}.
     *
     * @return the {@link Environment}
     */
    @Nonnull
    protected Environment getEnvironment() {
        return Environment.DEFAULT;
    }

    /**
     * Gets the {@link FileFetcher} to pass to {@link Configuration#setFileFetcher(FileFetcher)}.
     *
     * @return the {@link FileFetcher}
     */
    @CheckForNull
    protected FileFetcher getFileFetcher() {
        return null;
    }

}
