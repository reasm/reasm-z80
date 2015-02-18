package org.reasm.z80;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.reasm.*;
import org.reasm.source.SourceFile;
import org.reasm.testhelpers.UserSymbolMatcher;

import ca.fragag.Consumer;

/**
 * Test class for {@link Z80Architecture}.
 *
 * @author Francis Gagné
 */
public class Z80ArchitectureTest {

    @Nonnull
    private static final UnsignedIntValue ONE_HUNDRED = new UnsignedIntValue(100);
    @Nonnull
    private static final UnsignedIntValue TWENTY = new UnsignedIntValue(20);

    @Nonnull
    private static Assembly createAssembly1() {
        final PredefinedSymbol fooSymbol = new PredefinedSymbol(SymbolContext.VALUE, "foo", SymbolType.CONSTANT, ONE_HUNDRED);
        final PredefinedSymbol barSymbol = new PredefinedSymbol(SymbolContext.VALUE, "bar", SymbolType.CONSTANT, TWENTY);
        final PredefinedSymbolTable predefinedSymbols = new PredefinedSymbolTable(Arrays.asList(fooSymbol, barSymbol));

        final Configuration configuration = new Configuration(Environment.DEFAULT, new SourceFile("", ""), Z80Architecture.INSTANCE)
                .setPredefinedSymbols(predefinedSymbols);
        final Assembly assembly = new Assembly(configuration);
        return assembly;
    }

    /**
     * Asserts that {@link Z80Architecture#evaluateExpression(CharSequence, Assembly, Consumer, Consumer)} throws a
     * {@link NullPointerException} when the <code>assembly</code> argument is <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void evaluateExpressionNullAssembly() {
        Z80Architecture.INSTANCE.evaluateExpression("2+2", null, null, null);
    }

    /**
     * Asserts that {@link Z80Architecture#evaluateExpression(CharSequence, Assembly, Consumer, Consumer)} throws a
     * {@link NullPointerException} when the <code>expression</code> argument is <code>null</code>.
     */
    @Test(expected = NullPointerException.class)
    public void evaluateExpressionNullExpression() {
        final Configuration configuration = new Configuration(Environment.DEFAULT, new SourceFile("", ""), Z80Architecture.INSTANCE);
        final Assembly assembly = new Assembly(configuration);
        Z80Architecture.INSTANCE.evaluateExpression(null, assembly, null, null);
    }

    /**
     * Asserts that {@link Z80Architecture#evaluateExpression(CharSequence, Assembly, Consumer, Consumer)} evaluates an expression.
     */
    @Test
    public void evaluateExpressionNullSymbolReferenceConsumer() {
        final Assembly assembly = createAssembly1();

        final Consumer<AssemblyMessage> assemblyMessageConsumer = new Consumer<AssemblyMessage>() {
            @Override
            public void accept(AssemblyMessage assemblyMessage) {
                fail();
            }
        };

        final Value value = Z80Architecture.INSTANCE.evaluateExpression("foo+bar+3", assembly, null, assemblyMessageConsumer);
        assertThat(value, is((Value) new UnsignedIntValue(123)));
    }

    /**
     * Asserts that {@link Z80Architecture#evaluateExpression(CharSequence, Assembly, Consumer, Consumer)} correctly evaluates a
     * simple expression.
     */
    @Test
    public void evaluateExpressionSimple() {
        final Assembly assembly = createAssembly1();

        final Consumer<SymbolReference> symbolReferenceConsumer = new Consumer<SymbolReference>() {
            @Override
            public void accept(SymbolReference symbolReference) {
                fail();
            }
        };

        final Consumer<AssemblyMessage> assemblyMessageConsumer = new Consumer<AssemblyMessage>() {
            @Override
            public void accept(AssemblyMessage assemblyMessage) {
                fail();
            }
        };

        final Value value = Z80Architecture.INSTANCE.evaluateExpression("2+7*3", assembly, symbolReferenceConsumer,
                assemblyMessageConsumer);
        assertThat(value, is((Value) new UnsignedIntValue(23)));
    }

    /**
     * Asserts that {@link Z80Architecture#evaluateExpression(CharSequence, Assembly, Consumer, Consumer)} correctly evaluates an
     * expression that contains symbol references.
     */
    @Test
    public void evaluateExpressionWithSymbols() {
        final Assembly assembly = createAssembly1();

        final List<SymbolReference> symbolReferences = new ArrayList<>();
        final Consumer<SymbolReference> symbolReferenceConsumer = new Consumer<SymbolReference>() {
            @Override
            public void accept(SymbolReference symbolReference) {
                symbolReferences.add(symbolReference);
            }
        };

        final Consumer<AssemblyMessage> assemblyMessageConsumer = new Consumer<AssemblyMessage>() {
            @Override
            public void accept(AssemblyMessage assemblyMessage) {
                fail();
            }
        };

        final Value value = Z80Architecture.INSTANCE.evaluateExpression("foo+bar+3", assembly, symbolReferenceConsumer,
                assemblyMessageConsumer);
        assertThat(value, is((Value) new UnsignedIntValue(123)));

        assertThat(symbolReferences.size(), is(2));

        SymbolReference symbolReference;

        symbolReference = symbolReferences.get(0);
        assertThat(symbolReference, is(notNullValue()));
        assertThat(symbolReference.getContexts(), contains((Object) SymbolContext.VALUE));
        assertThat(symbolReference.getName(), is("foo"));
        assertThat(symbolReference.getSymbol(), is(notNullValue()));
        assertThat(symbolReference.getSymbol(), is(instanceOf(UserSymbol.class)));
        assertThat((UserSymbol) symbolReference.getSymbol(), is(new UserSymbolMatcher<>(SymbolContext.VALUE, "foo",
                SymbolType.CONSTANT, ONE_HUNDRED)));

        symbolReference = symbolReferences.get(1);
        assertThat(symbolReference, is(notNullValue()));
        assertThat(symbolReference.getContexts(), contains((Object) SymbolContext.VALUE));
        assertThat(symbolReference.getName(), is("bar"));
        assertThat(symbolReference.getSymbol(), is(notNullValue()));
        assertThat(symbolReference.getSymbol(), is(instanceOf(UserSymbol.class)));
        assertThat((UserSymbol) symbolReference.getSymbol(), is(new UserSymbolMatcher<>(SymbolContext.VALUE, "bar",
                SymbolType.CONSTANT, TWENTY)));
    }

    /**
     * Asserts that {@link Z80Architecture#Z80Architecture()} correctly initializes a {@link Z80Architecture}.
     */
    @Test
    public void Z80Architecture() {
        assertThat(Z80Architecture.INSTANCE.getNames(), contains("Z80"));
    }

}
