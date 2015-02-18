package org.reasm.z80;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.reasm.Architecture;

/**
 * Test class for {@link Z80ArchitectureProvider}.
 *
 * @author Francis Gagné
 */
public class Z80ArchitectureProviderTest {

    @Nonnull
    private static final Z80ArchitectureProvider Z80_ARCHITECTURE_PROVIDER = new Z80ArchitectureProvider();

    /**
     * Asserts that {@link Z80ArchitectureProvider#iterator()} returns an {@link Iterator} over the single {@link Z80Architecture}
     * object.
     */
    @Test
    public void iterator() {
        final Iterator<Architecture> iterator = Z80_ARCHITECTURE_PROVIDER.iterator();
        assertThat(iterator.next(), is(sameInstance((Architecture) Z80Architecture.INSTANCE)));
        assertThat(iterator.hasNext(), is(false));
    }

}
