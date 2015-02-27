package org.reasm.z80.assembly.internal;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * The conditions for jump, call and return instructions.
 * <p>
 * The enum constants are defined in the order that matches their encoding. Thus, {@link Enum#ordinal()} can be used to get the
 * encoding of a condition code.
 *
 * @author Francis Gagné
 */
@Immutable
enum Condition {

    // The order of these values must match their encoding, because Enum.ordinal() is used on this type.
    NZ, Z, NC, C, PO, PE, P, M;

    @Nonnull
    private static final Map<String, Condition> MAP;

    static {
        final Map<String, Condition> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        map.put("NZ", NZ);
        map.put("Z", Z);
        map.put("NC", NC);
        map.put("C", C);
        map.put("PO", PO);
        map.put("PE", PE);
        map.put("P", P);
        map.put("M", M);

        MAP = Collections.unmodifiableMap(map);
    }

    @CheckForNull
    static Condition parse(@Nonnull String code) {
        return MAP.get(code);
    }

}
