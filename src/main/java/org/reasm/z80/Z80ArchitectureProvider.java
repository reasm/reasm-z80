package org.reasm.z80;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.reasm.Architecture;
import org.reasm.ArchitectureProvider;

/**
 * The implementation of {@link ArchitectureProvider} for the Z80 architecture family.
 *
 * @author Francis Gagné
 */
@Immutable
public final class Z80ArchitectureProvider implements ArchitectureProvider {

    @Nonnull
    private static final Set<Architecture> ALL_ARCHITECTURES = Collections.singleton((Architecture) Z80Architecture.INSTANCE);

    @Override
    public Iterator<Architecture> iterator() {
        return ALL_ARCHITECTURES.iterator();
    }

}
